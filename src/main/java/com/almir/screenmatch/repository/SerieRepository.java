package com.almir.screenmatch.repository;

import com.almir.screenmatch.model.Categoria;
import com.almir.screenmatch.model.Episodio;
import com.almir.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long>  {

    Optional<Serie> findBytitleContainingIgnoreCase(String nomeSerie);

    //List<Serie> findByTotalSeasonsLessThanEqualAndAvaliacaoGreaterThanEqual(Integer totalTemporada, double serieAvaliacao);

    //Lembrando que o : refere-se ao construtor do metodo
    @Query("Select s From Serie s Where s.totalSeasons <= :totalTemporada AND s.avaliacao >= :serieAvaliacao")
    List<Serie> serieTemporadaEAvaliacao(Integer totalTemporada, double serieAvaliacao);

    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, double serieAvaliacao);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria categoria);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoEpisodio%")
    List<Episodio> buscarEpisodioPorTrecho(String trechoEpisodio);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> topEpisodiosPorSerie(Serie serie);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataLancamento) >= :anoLancamento")
    List<Episodio> episodiosPorSerieEAno(Serie serie, int anoLancamento);

    @Query("SELECT s FROM Serie s " +
            "JOIN s.episodios e " +
            "GROUP BY s " +
            "ORDER BY MAX(e.dataLancamento) DESC LIMIT 5")
    List<Serie> encontrarEpisodiosMaisRecentes();


    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id AND e.temporada = :numero")
    List<Episodio> obterEpisodiosPorTemporada(Long id, Long numero);
}
