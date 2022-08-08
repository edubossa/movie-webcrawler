package br.com.webcrawler.crawler;

import br.com.webcrawler.model.Movie;
import br.com.webcrawler.parser.MovieHtmlParser;
import br.com.webcrawler.parser.ReviewsHtmlParser;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Component
public class MovieWebCrawler {

    private static String BASE_URL = "https://www.imdb.com";
    private static UriComponentsBuilder REVIEWS_URL = UriComponentsBuilder.fromUriString(BASE_URL + "/title/{titleId}/reviews?sort=helpfulnessScore&dir=desc&ratingFilter=5");

    private final MovieHtmlParser movieParser = new MovieHtmlParser();
    private final ReviewsHtmlParser reviewParser = new ReviewsHtmlParser();

    private final TaskExecutor taskExecutor;

    @Autowired
    public MovieWebCrawler(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public List<Movie> getTopWorstMovies(int limit) throws InterruptedException {
        Document document = getHtmlFromWebPage(BASE_URL + "/chart/bottom");
        List<Movie> movieList = movieParser.parseMoviesFromRank(document, limit);
        CountDownLatch latch = new CountDownLatch(movieList.size());
        for(Movie movie : movieList) {
            taskExecutor.execute(() -> {
                getMovieDetails(movie);
                getMovieComments(movie);
                latch.countDown();
            });
        }
        latch.await();
        return movieList;
    }

    private void getMovieDetails(Movie movie) {
        Element html = getHtmlFromWebPage(BASE_URL + movie.getTitleUrl()).body();
        movieParser.parseMovieDetailsFromPage(html, movie);
    }

    private void getMovieComments(Movie movie) {
        Document reviewsHtml = getHtmlFromWebPage(REVIEWS_URL.buildAndExpand(movie.getTitleId()).toUriString());
        movie.setComments(reviewParser.parseComments(reviewsHtml, 3));
    }

    private Document getHtmlFromWebPage(String url){
        log.info("Getting from url [{}]", url);
        try {
            return Jsoup.connect(url)
                    .header("Accept-Language", "en-US,en;q=0.8,*;q=0.7")
                    .get();
        } catch (IOException e) {
            throw new RuntimeException("IOException when connecting to website", e);
        }
    }

}
