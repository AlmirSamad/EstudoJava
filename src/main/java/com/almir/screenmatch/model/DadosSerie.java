package com.almir.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(@JsonAlias("Title") String Title,
                         @JsonAlias("totalSeasons") String totalSeasons,
                         @JsonAlias("imdbRating") String avaliacao,
                         @JsonAlias("Genre") String genero,
                         @JsonAlias("Actors") String atores,
                         @JsonAlias("Poster") String poster,
                         @JsonAlias("Plot") String sinopse) {

    public Integer totalTemporada() {
        if (totalSeasons == null || totalSeasons.isBlank()) return null;
        return Integer.valueOf(totalSeasons);
    }

}
