package br.com.webcrawler.command;

import br.com.webcrawler.model.Movie;
import br.com.webcrawler.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MovieCommandLineRunner implements CommandLineRunner {

    private final MovieService movieService;

    @Override
    public void run(String... args) throws Exception {
        List<Movie> topWorstMovies = this.movieService.getMovies();

        log.info(topWorstMovies.toString());
    }

}
