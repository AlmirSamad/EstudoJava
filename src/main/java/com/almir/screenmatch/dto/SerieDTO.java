package com.almir.screenmatch.dto;

import com.almir.screenmatch.model.Categoria;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record SerieDTO(
        Long id,
        String title,
        Integer totalSeasons,
        Double avaliacao,
        Categoria genero,
        String atores,
        String poster,
        String sinopse) {

}
