package br.com.devdojo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Repository;

import br.com.devdojo.domain.Anime;
import br.com.devdojo.util.Utils;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AnimeRepository {

	private final Utils utils;
	private static List<Anime> animes;
	
	static {
		animes =new ArrayList<Anime>(List.of(
				new Anime(1, "DBZ"),
				new Anime(2, "Berserk"),
				new Anime(3, "Naruto")
				));
	}
	
	public Anime findById(int id){
		return utils.findAnimeOrThrowNotFound(id, animes);		
	}
	
	public List<Anime> listAll(){
		return animes;
	}
	
	public Anime save(Anime anime) {
		anime.setId(ThreadLocalRandom.current().nextInt(4,100000));
		animes.add(anime);
		return anime;
	}

	public void delete(int id) {
		animes.remove(utils.findAnimeOrThrowNotFound(id, animes));
	}

	public void update(Anime anime) {
		animes.remove(utils.findAnimeOrThrowNotFound(anime.getId(), animes));
		animes.add(anime);
	}
	
}
