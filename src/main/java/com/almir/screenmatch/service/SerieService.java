package com.almir.screenmatch.service;

import com.almir.screenmatch.dto.SerieDTO;
import com.almir.screenmatch.model.Serie;
import com.almir.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository serieRepository;


    private List<SerieDTO> converterDados(List<Serie> series){
        return  series
                .stream()
                .map(s -> new SerieDTO(s.getId(),s.getTitle(), s.getTotalSeasons(), s.getAvaliacao(), s.getGenero(), s.getAtores()
                        , s.getPoster(), s.getSinopse())).collect(Collectors.toList());
    }

    public List<SerieDTO> obterTodasSeries(){
        return converterDados(serieRepository.findAll());

    }

    public List<SerieDTO> obterTop5Series(){
        return converterDados(serieRepository.findTop5ByOrderByAvaliacaoDesc());
    }

    public List<SerieDTO> obterLancamento(){
        return converterDados(serieRepository.encontrarEpisodiosMaisRecentes());
    }
}
