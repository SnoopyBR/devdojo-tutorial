package br.com.devdojo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.devdojo.domain.Anime;
import br.com.devdojo.repository.AnimeRepository;
import br.com.devdojo.util.Utils;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AnimeService {

	private final Utils utils;
	private final AnimeRepository animeRepository;
	

	public Anime findById(int id){
		return utils.findAnimeOrThrowNotFound(id, animeRepository);		
	}
	
	public Page<Anime> listAll(Pageable pageable){
		return animeRepository.findAll(pageable);
	}
	
	@Transactional
	public Anime save(Anime anime) {
		return animeRepository.save(anime);
	}

	public void delete(int id) {
		animeRepository.delete(utils.findAnimeOrThrowNotFound(id, animeRepository));
	}

	public void update(Anime anime) {
		animeRepository.save(anime);
		
	}

	public List<Anime> findByName(String name) {
		return animeRepository.findByName(name);
	}
	
}
