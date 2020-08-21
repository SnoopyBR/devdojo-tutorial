package br.com.devdojo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.devdojo.domain.Anime;

public interface AnimeRepository extends JpaRepository<Anime, Integer>{
	
	List<Anime> findByName(String name);

}
