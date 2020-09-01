package br.com.devdojo.repository;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.devdojo.domain.Anime;
import br.com.devdojo.util.AnimeCreator;


@DataJpaTest
@DisplayName("Anime repository Tests")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AnimeRepositoryTest {

	
	@Autowired
	private AnimeRepository animeRepository;
	
	@Test
	@DisplayName("Save creates anime when successful")
	void save_PersistAnime_WhenSuccessful() {
		Anime anime = AnimeCreator.createAnimeToBeSaved();
		
		Anime savedAnime = this.animeRepository.save(anime);
		Assertions.assertThat(savedAnime.getId()).isNotNull();
		Assertions.assertThat(savedAnime.getName()).isNotNull();
		Assertions.assertThat(savedAnime.getName()).isEqualTo(anime.getName());
	}	
	
	@Test
	@DisplayName("Save updates anime when successful")
	void save_UpdateAnime_WhenSuccessful() {
		Anime anime = AnimeCreator.createAnimeToBeSaved();
		
		Anime savedAnime = this.animeRepository.save(anime);
		savedAnime.setName("That time I got reincarnated as a slime");
		Anime updatedAnime = this.animeRepository.save(savedAnime);
		Assertions.assertThat(savedAnime.getId()).isNotNull();
		Assertions.assertThat(savedAnime.getName()).isNotNull();
		Assertions.assertThat(savedAnime.getName()).isEqualTo(updatedAnime.getName());
	}	
	
	
	@Test
	@DisplayName("Delete removes anime when successful")
	void delete_RemoveAnime_WhenSuccessful() {
		Anime anime = AnimeCreator.createAnimeToBeSaved();
		
		Anime savedAnime = this.animeRepository.save(anime);
		
		this.animeRepository.delete(savedAnime);
		
		Optional<Anime> animeOptional  = this.animeRepository.findById(savedAnime.getId()); 
		Assertions.assertThat(animeOptional.isEmpty()).isTrue();
	}	
	
	
	@Test
	@DisplayName("Find By Name returns anime when successful")
	void findByName_ReturnAnimes_WhenSuccessful() {
		Anime anime = AnimeCreator.createAnimeToBeSaved();
		
		Anime savedAnime = this.animeRepository.save(anime);
		
		String name = savedAnime.getName(); 

		List<Anime> animeList = this.animeRepository.findByName(name);
		
		Assertions.assertThat(animeList).isNotEmpty().contains(savedAnime);
	}	
	
	@Test
	@DisplayName("Find By Name returns empty list when no anime is found")
	void findByName_ReturnEmptyList_WhenAnimeNotFound() {
		
		String name = "fake-name";

		List<Anime> animeList = this.animeRepository.findByName(name);
		
		Assertions.assertThat(animeList).isEmpty();
	}	
	
	@Test
	@DisplayName("Save throw ConstraintViolationException when name is empty")
	void save_ConstraintViolationException_WhenNameIsEmpty() {
		
		Anime anime = new Anime();


		
//		Assertions.assertThatThrownBy(() -> animeRepository.save(anime))
//		.isInstanceOf(ConstraintViolationException.class);
		
		Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
		.isThrownBy(() -> animeRepository.save(anime))
		.withMessageContaining("The name of this anime cannot be empty");
	}	


}
