package br.com.devdojo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.devdojo.model.Anime;
import br.com.devdojo.repository.AnimeRepository;
import br.com.devdojo.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("animes")
@Slf4j
@RequiredArgsConstructor
public class AnimeController {

	private final Utils dateUtil;
	private final AnimeRepository animeRepository;

	@GetMapping()
	public ResponseEntity<List<Anime>> listAll() {
		log.info("Formating the date {}", dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
		return  ResponseEntity.ok(animeRepository.listAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Anime> findById(@PathVariable int id){
		return ResponseEntity.ok(animeRepository.findById(id));
	}

	@PostMapping
	public ResponseEntity<Anime> save(@RequestBody Anime anime){
		return ResponseEntity.ok(animeRepository.save(anime));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable int id){
		log.info("Deleting anime with id {}",id);
		animeRepository.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@PutMapping
	public ResponseEntity<Void> update(@RequestBody Anime anime){
		animeRepository.update(anime);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
}
