package br.com.devdojo.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import br.com.devdojo.controller.exception.ResourceNotFoundException;
import br.com.devdojo.domain.Anime;
import br.com.devdojo.repository.AnimeRepository;

@Component
public class Utils {
	
	public String formatLocalDateTimeToDatabaseStyle(LocalDateTime localDateTime) {
		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
	}
		
	public Anime findAnimeOrThrowNotFound(int id, AnimeRepository animeRepository) {
		return animeRepository
				.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException("Anime not found"));
	}
}
