package br.com.webcrawler.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Movie {

    private String titleId;
    private String name;
    private float rating;
    private int rank;
    private List<String> directors;
    private List<String> mainCasts;
    private List<String> comments;
    private String titleUrl;

    @Override
    public String toString() {
        return "\nMovie{" +
                "\n\tname='" + name + '\'' +
                ", \n\trating=" + rating +
                ", \n\trank=" + rank +
                ", \n\tdirectors=" + directors +
                ", \n\tmainCasts=" + mainCasts +
                ", \n\tcomments=" + comments +
                "\n}";
    }
}
