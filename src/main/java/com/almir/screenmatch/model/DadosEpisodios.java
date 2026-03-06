package com.almir.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodios(@JsonAlias("Title") String Titulo,
                             @JsonAlias("Episode") Integer episodio,
                             @JsonAlias("imdbRating") String avaliacao,
                             @JsonAlias("Released") String lancamento) {
}
