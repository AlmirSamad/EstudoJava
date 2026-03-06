package com.almir.screenmatch.controller;


import com.almir.screenmatch.dto.SerieDTO;
import com.almir.screenmatch.model.Serie;
import com.almir.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SerieController {

    @Autowired
    private SerieRepository serieRepository;

    @GetMapping("/series")
    public List<SerieDTO> obterSeries(){
       return serieRepository.findAll()
               .stream()
               .map(s -> new SerieDTO(s.getId(),s.getTitle(), s.getTotalSeasons(), s.getAvaliacao(), s.getGenero(), s.getAtores()
               , s.getPoster(), s.getSinopse())).collect(Collectors.toList());
    }
}
