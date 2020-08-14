package br.com.devdojo.endpoint;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.devdojo.model.Anime;
import br.com.devdojo.repository.AnimeRepository;
import br.com.devdojo.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("animes")
@Slf4j
@RequiredArgsConstructor
public class AnimeController {

	private final DateUtil dateUtil;
	private final AnimeRepository animeRepository;

	@GetMapping()
	public ResponseEntity<List<Anime>> listAll() {
		log.info("Formating the date {}", dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
		return  ResponseEntity.ok(animeRepository.listAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Anime> findById(@PathVariable long id){
		Anime animeFound = animeRepository.listAll()
		.stream()
		.filter(anime -> anime.getId()==id)
		.findFirst()
		.orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Anime not found"));
		return ResponseEntity.ok(animeFound);
	}

}
