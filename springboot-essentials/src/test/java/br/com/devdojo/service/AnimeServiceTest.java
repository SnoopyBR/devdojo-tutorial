package br.com.devdojo.service;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.devdojo.controller.exception.ResourceNotFoundException;
import br.com.devdojo.domain.Anime;
import br.com.devdojo.repository.AnimeRepository;
import br.com.devdojo.util.AnimeCreator;
import br.com.devdojo.util.Utils;


@ExtendWith(SpringExtension.class)
class AnimeServiceTest {

	@InjectMocks
	private AnimeService animeService;
	
	
	@Mock 
	private AnimeRepository animeRepositoryMock;
	
	@Mock 
	private Utils utilsMock;
	
	@BeforeEach
	public void setUp() {
		
		PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
		
		BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
		.thenReturn(animePage);
		
		BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyInt()))
		.thenReturn(Optional.of(AnimeCreator.createValidAnime()));
		
		BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
		.thenReturn(List.of(AnimeCreator.createValidAnime()));
		
		BDDMockito.when(animeRepositoryMock.save(AnimeCreator.createAnimeToBeSaved()))
		.thenReturn(AnimeCreator.createValidAnime());

		BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));

		BDDMockito.when(animeRepositoryMock.save(AnimeCreator.createValidAnime()))
		.thenReturn(AnimeCreator.createValidUpdatedAnime());

		BDDMockito.when(utilsMock.findAnimeOrThrowNotFound(ArgumentMatchers.anyInt(), ArgumentMatchers.any(AnimeRepository.class)))
				.thenReturn(AnimeCreator.createValidUpdatedAnime());
		
	}
	
	
	@Test
	@DisplayName("listAll returns a pageable list of animes when successful")
	void listAll_ReturnListOfAnimesInsidePageObject_WhenSuccessful() {
		
		String expectedName = AnimeCreator.createValidAnime().getName();
		
		Page<Anime> animePage = animeService.listAll(PageRequest.of(1, 1));
		
		Assertions.assertThat(animePage).isNotNull();
		
		Assertions.assertThat(animePage.toList()).isNotEmpty();
		
		Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
		
	}

	@Test
	@DisplayName("findById returns an anime when successful")
	void findById_ReturnAnAnime_WhenSuccessful() {
		
		Integer expectedId = AnimeCreator.createValidAnime().getId();
		
		Anime anime = animeService.findById(1);
		
		Assertions.assertThat(anime).isNotNull();
		
		Assertions.assertThat(anime.getId()).isNotNull();
		
		Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
		
		
	}
	
	
	@Test
	@DisplayName("findByName returns a pageable list of animes when successful")
	void findByName_ReturnListOfAnimesInsidePageObject_WhenSuccessful() {
		
		String expectedName = AnimeCreator.createValidAnime().getName();
		
		List<Anime> animeList = animeService.findByName("");
			
		Assertions.assertThat(animeList).isNotNull().isNotEmpty();
		
		Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
		
		
	}
	

	@Test
	@DisplayName("save creates a anime when successful")
	void save_CreatesAnAnime_WhenSuccessful() {
		
		Integer expectedId = AnimeCreator.createValidAnime().getId();
		
		Anime animeToBeSaver = AnimeCreator.createAnimeToBeSaved();
		
		Anime anime = animeService.save(animeToBeSaver);
		
		Assertions.assertThat(anime).isNotNull();
		
		Assertions.assertThat(anime.getId()).isNotNull();
		
		Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
				
	}
		
	@Test
	@DisplayName("delete removes the anime when successful")
	void delete_removesAnime_WhenSuccessful() {
		
		Assertions.assertThatCode(() -> animeService.delete(1))
		.doesNotThrowAnyException();
				
	}
	
	@Test
	@DisplayName("delete throw ResourceNotFoundException when the animes does not exist")
	void delete_ThrowsResourceNotFoundException_WhenAnimeDoesNotExist() {
		
		BDDMockito.when(utilsMock.findAnimeOrThrowNotFound(ArgumentMatchers.anyInt(), ArgumentMatchers.any(AnimeRepository.class)))
				.thenThrow(new ResourceNotFoundException("Anime not found"));
		
		Assertions.assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() -> animeService.delete(1));
				
	}
	
	@Test
	@DisplayName("update updating update anime when successful")
	void save_SaveUpdatedAnime_WhenSuccessful() {
		
		Anime validUpdatedAnime = AnimeCreator.createValidUpdatedAnime();
		
		String expectedName = validUpdatedAnime.getName();
		
		Anime anime = animeService.save(AnimeCreator.createValidAnime());
				
		Assertions.assertThat(anime).isNotNull();
		
		Assertions.assertThat(anime.getId()).isNotNull();
		
		Assertions.assertThat(anime.getName()).isEqualTo(expectedName);
		
		
	}
	

}
