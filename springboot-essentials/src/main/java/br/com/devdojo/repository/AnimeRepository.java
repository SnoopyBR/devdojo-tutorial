package br.com.devdojo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import br.com.devdojo.model.Anime;

@Repository
public class AnimeRepository {

	private static List<Anime> animes;
	
	static {
		animes =new ArrayList<Anime>(List.of(
				new Anime(1, "DBZ"),
				new Anime(2, "Berserk"),
				new Anime(3, "Naruto")
				));
	}
	
	public List<Anime> listAll(){
		return animes;
	}
	
	public Anime save(Anime anime) {
		anime.setId(ThreadLocalRandom.current().nextInt(4,100000));
		animes.add(anime);
		return anime;
	}

	public void delete(long id) {
		animes.remove(animes
				.stream()
				.filter(anime -> anime.getId() == id)
				.findFirst()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Anime not found"))
				);
		
		
	}
	
}
