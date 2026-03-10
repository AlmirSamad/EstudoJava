package com.almir.screenmatch.service;

import com.almir.screenmatch.dto.EpisodioDTO;
import com.almir.screenmatch.dto.SerieDTO;
import com.almir.screenmatch.model.Serie;
import com.almir.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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


    public SerieDTO obterPorId(Long id) {
        Optional<Serie> serie = serieRepository.findById(id);

        if (serie.isPresent()){
            Serie s = serie.get();
            return new SerieDTO(s.getId(),s.getTitle(), s.getTotalSeasons(), s.getAvaliacao(), s.getGenero(), s.getAtores()
                    , s.getPoster(), s.getSinopse());
        }
        return  null;
    }

    public List<EpisodioDTO> obterTodasTemporadas(Long id) {

        Optional<Serie> serie = serieRepository.findById(id);

        if (serie.isPresent()){
            Serie s = serie.get();
            return  s.getEpisodios().stream()
                    .map(e -> new EpisodioDTO(e.getTitulo(), e.getTemporada(), e.getEpisodio()))
                    .collect(Collectors.toList());
        }
        return  null;

    }

    public List<EpisodioDTO> obterEpisodioPorTemporada(Long id, Long numero){
        return serieRepository.obterEpisodiosPorTemporada(id, numero)
                .stream()
                .map(e -> new EpisodioDTO(e.getTitulo(), e.getTemporada(), e.getEpisodio()))
                .collect(Collectors.toList());
    }
}
