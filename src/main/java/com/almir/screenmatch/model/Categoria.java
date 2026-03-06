package com.almir.screenmatch.model;

public enum Categoria {
    ACAO("Action", "Ação"),
    ROMANCE("Romance", "Romance"),
    COMEDIA("Comedy", "Comedia"),
    CRIME("Crime", "Crime"),
    TERROR("Horror", "Terror"),
    SUSPENSE("Thriller", "Suspense"),
    DRAMA("Drama", "Drama"),
    ANIMACAO("Animation", "Animação"),
    DESCONHECIDO("Unknown", "Desconhecido");


    private String categoriaOmdb;
    private String categoriaOmdbPT;

    Categoria(String categoriaOmdb, String categoriaOmdbPT){

        this.categoriaOmdb = categoriaOmdb;
        this.categoriaOmdbPT = categoriaOmdbPT;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        return Categoria.DESCONHECIDO;
    }

    public static Categoria fromPortugues(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdbPT.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        return Categoria.DESCONHECIDO;
    }


}
