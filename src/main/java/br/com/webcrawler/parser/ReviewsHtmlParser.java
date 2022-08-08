package br.com.webcrawler.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

public class ReviewsHtmlParser {

    public List<String> parseComments(Document html, int numberOfComments){
        return html.body()
                .select(".lister-item.imdb-user-review .content .text")
                .stream()
                .limit(numberOfComments)
                .map(Element::text)
                .toList();

    }

}
