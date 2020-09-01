package br.com.devdojo.util;

import br.com.devdojo.domain.Anime;

public class AnimeCreator {
	public static Anime createAnimeToBeSaved() {
		return Anime.builder().name("Tensei Shitara Slime Datta Ken").build();
	}
	
	public static Anime createValidAnime() {
		return Anime
				.builder()
				.id(1)
				.name("Tensei Shitara Slime Datta Ken")
				.build();
	}
	
	public static Anime createValidUpdatedAnime() {
		return Anime
				.builder()
				.id(1)
				.name("Tensei Shitara Slime Datta Ken 2")
				.build();
	}
}
