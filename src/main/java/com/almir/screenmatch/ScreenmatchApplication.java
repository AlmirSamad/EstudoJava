package com.almir.screenmatch;

import com.almir.screenmatch.model.DadosEpisodios;
import com.almir.screenmatch.model.DadosSerie;
import com.almir.screenmatch.model.DadosTemporada;
import com.almir.screenmatch.principal.Principal;
import com.almir.screenmatch.repository.SerieRepository;
import com.almir.screenmatch.service.ConsultaGemini;
import com.almir.screenmatch.service.ConsumoApi;
import com.almir.screenmatch.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

        @Autowired
        private final Principal principal;


    public ScreenmatchApplication(Principal principal) {
        this.principal = principal;
    }

    @Override
    public void run(String... args) throws Exception {
      principal.exibirMenu();

    }
}
