package br.com.webcrawler.service;

import br.com.webcrawler.crawler.MovieWebCrawler;
import br.com.webcrawler.model.Movie;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieWebCrawler movieWebCrawler;

    @Cacheable(value = "MovieWebcrawlerApi",
            key = "'webcrawler'",
            cacheManager = "cacheManager")
    @SneakyThrows
    public List<Movie> getMovies() {
        return this.movieWebCrawler.getTopWorstMovies(10);
    }

}
