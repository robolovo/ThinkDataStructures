package com.allendowney.thinkdast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WikiPhilosophy {
    private final static String WIKI_BASE_URL = "https://en.wikipedia.org";

    final static List<String> visited = new ArrayList<String>();
    final static WikiFetcher wf = new WikiFetcher();

    /**
     * Tests a conjecture about Wikipedia and Philosophy.
     *
     * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
     *
     * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String destination = "https://en.wikipedia.org/wiki/Philosophy";
        String source = "https://en.wikipedia.org/wiki/Java_(programming_language)";

        testConjecture(destination, source, 10);
    }

    /**
     * Starts from given URL and follows first link until it finds the destination or exceeds the limit.
     *
     * @param destination
     * @param source
     * @throws IOException
     */
    public static void testConjecture(String destination, String source, int limit) throws IOException {
        if (visited.contains(source)) {
            System.err.println("We are in loop!");
            return ;
        }
        visited.add(source);

        if (limit < 1) {
            return ;
        }

        if (source.equals(destination)) {
            System.out.println("Success!");
            return ;
        }

        Elements elements = wf.fetchWikipedia(source);
        WikiParser wp = new WikiParser(elements);

        Element firstLink = wp.findFirstLink();
        System.out.println("First valid link in current page: " + firstLink.html());

        String[] ss = firstLink.toString().split(" ");
        StringBuilder url = new StringBuilder(WIKI_BASE_URL);
        for (String s : ss) {
            if (s.startsWith("href=\"")) {
                url.append(s.substring(6, s.length() - 1));
            }
        }

        testConjecture(destination, url.toString(), limit - 1);
    }
}
