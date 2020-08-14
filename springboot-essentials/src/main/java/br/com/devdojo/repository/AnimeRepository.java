package br.com.devdojo.repository;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.devdojo.model.Anime;

@Repository
public class AnimeRepository {

	public List<Anime> listAll(){
		
		return Arrays.asList(
				new Anime(1, "DBZ"),
				new Anime(2, "Berserk"),
				new Anime(3, "Naruto")
				);
		
	}
	
}
