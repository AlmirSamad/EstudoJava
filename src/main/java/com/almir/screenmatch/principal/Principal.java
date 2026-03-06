package com.almir.screenmatch.principal;


import com.almir.screenmatch.model.*;
import com.almir.screenmatch.repository.SerieRepository;
import com.almir.screenmatch.service.ConsultaGemini;
import com.almir.screenmatch.service.ConsumoApi;
import com.almir.screenmatch.service.ConverteDados;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class Principal {

    private final OmdbConfig config;
    private final ConsumoApi consumoApi;
    private final ConverteDados converteDados;
    private final Scanner sc = new Scanner(System.in);
    private final List<DadosTemporada> temp = new ArrayList<>();
    private SerieRepository serieRepository;
    private ConsultaGemini consultaGemini;
    private List<Serie> series = new ArrayList<>();
    private Optional<Serie> serieBusca;
    public Principal(OmdbConfig config,
                     ConsumoApi consumoApi,
                     ConverteDados converteDados, SerieRepository serieRepository, ConsultaGemini consultaGemini) {
        this.config = config;
        this.consumoApi = consumoApi;
        this.converteDados = converteDados;
        this.serieRepository = serieRepository;
        this.consultaGemini = consultaGemini;
    }

    public void exibirMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Lista de séries
                    4 - Buscar série por título
                    5 - Buscar série por Ator
                    6 - Top 5 Séries
                    7 - Buscar por Categoria
                    8 - Busca personalizada
                    9 - Buscar episódio por série
                    10 - Top 5 episódios por série
                    11 - Buscar episódios a partir de uma data
                   
                    0 - Sair
                    """;

            System.out.println(menu);
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriePorCategoria();
                    break;
                case 8:
                    buscarPersonalizada();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    topEpisodiosPorSerie();
                    break;
                case 11:
                    buscarEpisodiosDepoisDeUmaData();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }



    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();

        // 1. Verificar se a série já existe no banco PELO TÍTULO
        Optional<Serie> serieExiste = serieRepository.findBytitleContainingIgnoreCase(dados.Title());

        if (serieExiste.isPresent()) {
            System.out.println("Série '" + dados.Title() + "' já existe no banco de dados!");
        } else {
            // 2. Se não existe, aí sim fazemos o processo de tradução e salvamento
            String sinopseTraduzida = consultaGemini.obterTraducao(dados.sinopse());
            Serie serie = new Serie(dados);
            serie.setSinopse(sinopseTraduzida);

            serieRepository.save(serie);
            System.out.println("Série salva com sucesso!");
            System.out.println(serie);
        }
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = URLEncoder.encode(sc.nextLine(), StandardCharsets.UTF_8);
        var endereco = config.getBaseurl() + nomeSerie + config.getApikey();
        var json = consumoApi.obterDados(endereco);
        DadosSerie dados = converteDados.obterDados(json, DadosSerie.class);
        return dados;
    }
    private void buscarEpisodioPorSerie(){
        listarSeriesBuscadas();
        System.out.println("Qual série você quer sbaer? ");
        var nomeSerie = sc.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitle().toLowerCase().contains(nomeSerie.toLowerCase()))
                .findFirst();


        if(serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalSeasons(); i++) {
                String titulo = URLEncoder.encode(serieEncontrada.getTitle(), StandardCharsets.UTF_8);
                String endereco = config.getBaseurl() + titulo + "&season=" + i + config.getApikey();

                String json = consumoApi.obterDados(endereco);
                DadosTemporada dadosTemporada = converteDados.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios =  temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            serieRepository.save(serieEncontrada);


        }else {
            System.out.println("Série não encontrada!");
        }
    }

    private List<DadosSerie> dadosSeries = new ArrayList<>();

    private void listarSeriesBuscadas() {

        series = serieRepository.findAll();

        if (series.isEmpty()) {
            System.out.println("Nenhuma série cadastrada.");
            return;
        }

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Qual série você quer saber? ");
        var nomeSerie = sc.nextLine();
        serieBusca = serieRepository.findBytitleContainingIgnoreCase(nomeSerie);

        if (serieBusca.isPresent()){
            System.out.println("Dados da série " + serieBusca.get());
        }else {
            System.out.println("Série não encontrada");
        }
    }

    private void buscarSeriePorAtor(){
        System.out.println("Busar trabalhos por Ator: ");
        var nomeAtor = sc.nextLine();
        System.out.println("A partir de qual nota? ");
        var serieAvaliacao = sc.nextDouble();

        List<Serie> seriePorAtor = serieRepository.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, serieAvaliacao);

        System.out.println("Séries em que "+nomeAtor+" trabalhou: ");
        seriePorAtor.forEach(s -> System.out.println(s.getTitle() + " Avaliação: " + s.getAvaliacao()));
    }

    private void buscarTop5Series(){
        List<Serie> top5Series = serieRepository.findTop5ByOrderByAvaliacaoDesc();
        top5Series.forEach(s ->
                System.out.println( s.getTitle() + " avaliação: " + s.getAvaliacao()));
    }

    private void buscarSeriePorCategoria(){

        System.out.println("Qual gênero deseja assistir? ");

        var genero = sc.nextLine();

        Categoria categoria = Categoria.fromPortugues(genero);

        series = serieRepository.findByGenero(categoria);

        series.forEach(System.out::println);
    }

    private  void buscarPersonalizada(){
        System.out.println("Qual é a quantidade de temporadas que deseja assistir? ");
        var totalTemp = sc.nextInt();
        System.out.println("Qual avaliação deseja assitir?");
        var avaliacao = sc.nextDouble();

        series = serieRepository.serieTemporadaEAvaliacao(totalTemp, avaliacao);

        series.forEach(s -> System.out.println(s.getTitle() + " Temporadas: " +s.getTotalSeasons() + " Avaliação: " + s.getAvaliacao()));
    }

    private void buscarEpisodioPorTrecho(){
        System.out.println("Qual é o título do episódio: ");
        var trechoEpisodio = sc.nextLine();
        List<Episodio> episodiosEncontrados = serieRepository.buscarEpisodioPorTrecho(trechoEpisodio);

        episodiosEncontrados.forEach(e ->
                System.out.printf("Série %s Temporada %s - Episódio %s - %s\n",
                        e.getSerie().getTitle(), e.getTemporada(), e.getEpisodio(), e.getTitulo()));
    }

    private void topEpisodiosPorSerie(){
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = serieRepository.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e ->
                    System.out.printf("Série %s Temporada %s - Episódio %s - %s Avaliacao %s\n",
                            e.getSerie().getTitle(), e.getTemporada(), e.getEpisodio(), e.getTitulo(), e.getAvaliacao()));
        }
    }

    private void buscarEpisodiosDepoisDeUmaData(){
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            System.out.println("A partir de qual data vc quer buscar? ");
            var anoLancamento = sc.nextInt();
            sc.nextLine();
            List<Episodio> episodiosAno = serieRepository.episodiosPorSerieEAno(serie, anoLancamento);

            episodiosAno.forEach(e ->
                    System.out.printf("Série %s Temporada %s - Episódio %s - %s Avaliacao %s\n",
                            e.getSerie().getTitle(), e.getTemporada(), e.getEpisodio(), e.getTitulo(), e.getAvaliacao()));
        }
    }


}
