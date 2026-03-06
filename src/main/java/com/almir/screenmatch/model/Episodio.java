package com.almir.screenmatch.model;



import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "episodios")
public class Episodio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   private String titulo;
   private Integer temporada;
   private Integer episodio;
   private Double avaliacao;
   private LocalDate dataLancamento;

   @ManyToOne
   @JoinColumn(name = "serie_id")
   private Serie serie;

    public Episodio() {}

    public Episodio(Integer numeroTemporadas, DadosEpisodios dadosEpisodios) {
        this.titulo = dadosEpisodios.Titulo();
        this.temporada = numeroTemporadas;
        this.episodio = dadosEpisodios.episodio();
        try {
            this.avaliacao = Double.valueOf(dadosEpisodios.avaliacao()) ;
        } catch (NumberFormatException e) {
            this.avaliacao = 0.0;
        }
        try{
            this.dataLancamento = LocalDate.parse(dadosEpisodios.lancamento());
        } catch (Exception e) {
            dataLancamento = null;
        }

    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        titulo = titulo;
    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public Integer getEpisodio() {
        return episodio;
    }

    public void setEpisodio(Integer episodio) {
        this.episodio = episodio;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLacamento(LocalDate dataLacamento) {
        this.dataLancamento = dataLacamento;
    }

    @Override
    public String toString() {
        return
                "Titulo='" + titulo + '\'' +
                ", temporada=" + temporada +
                ", episodio=" + episodio +
                ", avaliacao=" + avaliacao +
                ", dataLacamento=" + dataLancamento
                ;
    }

}
