package br.com.webcrawler.parser;

import br.com.webcrawler.model.Movie;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Collections;
import java.util.List;

public class MovieHtmlParser {

    public List<Movie> parseMoviesFromRank(Document html, int limit) {
        Elements listElements = getListElements(html, limit);

        return listElements.stream()
                .map(element -> Movie.builder()
                        .titleId(getTitleId(element))
                        .name(getTitleName(element))
                        .rank(getTitleRank(element))
                        .titleUrl(getTitleUrl(element))
                        .rating(getRating(element))
                        .build())
                .toList();

    }

    public void parseMovieDetailsFromPage(Element htmlBody, Movie movie){
        movie.setDirectors(getDirectors(htmlBody));
        movie.setMainCasts(getActors(htmlBody));
    }

    public Elements getListElements(Document html, int limit) {
        List<Element> list = html.select("tbody.lister-list>tr").stream()
                .limit(limit)
                .toList();
        return new Elements(list);
    }

    private String getTitleUrl(Element titleElement) {
        return titleElement.select("a[href]").stream()
                .map(a -> a.attr("href"))
                .filter(href -> href.startsWith("/title/"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("title link not found"));
    }

    private String getTitleId(Element titleElement){
        return titleElement.select(".ratingColumn>.seen-widget").attr("data-titleid");
    }

    private String getTitleName(Element titleElement){
        return titleElement.selectFirst(".titleColumn>a").text();
    }

    private int getTitleRank(Element titleElement) {
        String rank = titleElement.getElementsByAttributeValue("name", "rk").iterator().next().attr("data-value");
        return Integer.parseInt(rank);
    }

    private List<String> getDirectors(Element body) {
        List<String> directors = getPeople(body, "Directors");
        return directors.isEmpty() ? getPeople(body, "Director") : directors ;
    }

    private List<String> getActors(Element body) {
        return getPeople(body, "Stars");
    }

    private float getRating(Element body) {
        String ratingString = body.selectFirst(".ratingColumn.imdbRating")
                .text();
        return Float.parseFloat(ratingString);
    }

    private List<String> getPeople(Element body, String type){
        return body.getElementsByAttributeValue("data-testid", "title-pc-principal-credit").stream()
                .filter(element ->
                        element.selectFirst(".ipc-metadata-list-item__label").text().contains(type))
                .findFirst()
                .map(directorsDiv ->
                        directorsDiv.select(".ipc-metadata-list-item__list-content-item").stream()
                                .map(Element::text)
                                .toList())
                .orElse(Collections.emptyList());
    }

}
