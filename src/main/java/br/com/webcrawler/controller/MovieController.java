package br.com.webcrawler.controller;

import br.com.webcrawler.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/")
    public String movies(Model model) {
        model.addAttribute("movies", this.movieService.getMovies());
        return "movie";
    }

}
