package com.dawan.cinema.seed;

import java.util.List;

/**
 * Données de démonstration : vrais cinémas (adresses vérifiées) et vrais films.
 * Le nombre de salles est simplifié (3 à 5 par cinéma).
 */
final class SeedData {

    record CinemaSeed(String nom, int numero, String rue, String ville, int salles) {
    }

    record MovieSeed(String titre, int annee, String genre) {
    }

    static final List<CinemaSeed> CINEMAS = List.of(
            new CinemaSeed("Pathé Bellecour", 79, "Rue de la République", "Lyon", 5),
            new CinemaSeed("Cinéma Comoedia", 13, "Avenue Berthelot", "Lyon", 4),
            new CinemaSeed("Lumière Terreaux", 40, "Rue du Président Édouard Herriot", "Lyon", 3),
            new CinemaSeed("Le Grand Rex", 1, "Boulevard Poissonnière", "Paris", 5),
            new CinemaSeed("MK2 Bibliothèque", 128, "Avenue de France", "Paris", 5),
            new CinemaSeed("Le Majestic", 54, "Rue de Béthune", "Lille", 4),
            new CinemaSeed("UGC Ciné Cité Lille", 40, "Rue de Béthune", "Lille", 4));

    static final List<MovieSeed> MOVIES = List.of(
            new MovieSeed("Dune : Deuxième partie", 2024, "Science-fiction"),
            new MovieSeed("Oppenheimer", 2023, "Drame historique"),
            new MovieSeed("Interstellar", 2014, "Science-fiction"),
            new MovieSeed("Inception", 2010, "Science-fiction"),
            new MovieSeed("Le Fabuleux Destin d'Amélie Poulain", 2001, "Comédie"),
            new MovieSeed("Intouchables", 2011, "Comédie"),
            new MovieSeed("La La Land", 2016, "Comédie musicale"),
            new MovieSeed("Parasite", 2019, "Thriller"),
            new MovieSeed("Pulp Fiction", 1994, "Policier"),
            new MovieSeed("Le Voyage de Chihiro", 2001, "Animation"),
            new MovieSeed("Spider-Man : Across the Spider-Verse", 2023, "Animation"),
            new MovieSeed("Vice-versa 2", 2024, "Animation"),
            new MovieSeed("Coco", 2017, "Animation"),
            new MovieSeed("Barbie", 2023, "Comédie"),
            new MovieSeed("Anatomie d'une chute", 2023, "Drame"),
            new MovieSeed("Top Gun : Maverick", 2022, "Action"),
            new MovieSeed("Mad Max : Fury Road", 2015, "Action"),
            new MovieSeed("Gladiator", 2000, "Péplum"),
            new MovieSeed("Le Comte de Monte-Cristo", 2024, "Aventure"),
            new MovieSeed("Le Seigneur des anneaux : La Communauté de l'anneau", 2001, "Fantasy"));

    private SeedData() {
    }
}