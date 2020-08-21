package br.com.devdojo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.devdojo.domain.Anime;
import br.com.devdojo.service.AnimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("animes")
@Slf4j
@RequiredArgsConstructor
public class AnimeController {

	private final AnimeService animeService;

	@GetMapping()
	public ResponseEntity<List<Anime>> listAll() {
		return ResponseEntity.ok(animeService.listAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Anime> findById(@PathVariable int id) {
		return ResponseEntity.ok(animeService.findById(id));
	}

	@GetMapping("/find")
	public ResponseEntity<List<Anime>> findByName(@RequestParam(value = "name") String name) {
		return ResponseEntity.ok(animeService.findByName(name));
	}

	@PostMapping
	public ResponseEntity<Anime> save(@RequestBody @Valid  Anime anime) {
		return ResponseEntity.ok(animeService.save(anime));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable int id) {
		log.info("Deleting anime with id {}", id);
		animeService.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PutMapping
	public ResponseEntity<Void> update(@RequestBody Anime anime) {
		animeService.update(anime);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
