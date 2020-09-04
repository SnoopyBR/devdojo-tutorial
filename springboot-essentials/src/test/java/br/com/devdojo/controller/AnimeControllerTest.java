package br.com.devdojo.controller;

import java.util.List;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.devdojo.domain.Anime;
import br.com.devdojo.service.AnimeService;
import br.com.devdojo.util.AnimeCreator;


@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

	
	@InjectMocks
	private AnimeController animeController;
	
	@Mock
	private AnimeService animeServiceMock;
	
	@BeforeEach
	public void setUp() {
		PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
		BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))
		.thenReturn(animePage);
		
		BDDMockito.when(animeServiceMock.findById(ArgumentMatchers.anyInt()))
		.thenReturn(AnimeCreator.createValidAnime());
		
		BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
		.thenReturn(List.of(AnimeCreator.createValidAnime()));
		
		BDDMockito.when(animeServiceMock.save(AnimeCreator.createAnimeToBeSaved()))
		.thenReturn(AnimeCreator.createValidAnime());

		BDDMockito.doNothing().when(animeServiceMock).delete(ArgumentMatchers.anyInt());

		BDDMockito.when(animeServiceMock.save(AnimeCreator.createValidAnime()))
		.thenReturn(AnimeCreator.createValidUpdatedAnime());

		
	}
	
	
	@Test
	@DisplayName("listAll returns a pageable list of animes when successful")
	void listAll_ReturnListOfAnimesInsidePageObject_WhenSuccessful() {
		
		String expectedName = AnimeCreator.createValidAnime().getName();
		Page<Anime> animePage = animeController.listAll(null).getBody();
		
		Assertions.assertThat(animePage).isNotNull();
		
		Assertions.assertThat(animePage.toList()).isNotEmpty();
		
		Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
		
		
	}

	@Test
	@DisplayName("findById returns an anime when successful")
	void findById_ReturnAnAnime_WhenSuccessful() {
		
		Integer expectedId = AnimeCreator.createValidAnime().getId();
		
		Anime anime = animeController.findById(1).getBody();
		
		Assertions.assertThat(anime).isNotNull();
		
		Assertions.assertThat(anime.getId()).isNotNull();
		
		Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
		
		
	}
	
	
	@Test
	@DisplayName("findByName returns a list of animes when successful")
	void findByName_ReturnListOfAnimes_WhenSuccessful() {
		
		String expectedName = AnimeCreator.createValidAnime().getName();
		
		List<Anime> animeList = animeController.findByName("").getBody();
			
		Assertions.assertThat(animeList).isNotNull().isNotEmpty();
		
		Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
		
		
	}
	

	@Test
	@DisplayName("save creates a anime when successful")
	void save_CreatesAnAnime_WhenSuccessful() {
		
		Integer expectedId = AnimeCreator.createValidAnime().getId();
		
		Anime animeToBeSaver = AnimeCreator.createAnimeToBeSaved();
		
		Anime anime = animeController.save(animeToBeSaver).getBody();
		
		Assertions.assertThat(anime).isNotNull();
		
		Assertions.assertThat(anime.getId()).isNotNull();
		
		Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
		
		
	}
	
	
	@Test
	@DisplayName("delete removes the anime when successful")
	void delete_removesAnime_WhenSuccessful() {
		

		ResponseEntity<Void > responseEntity = animeController.delete(1);
		
		Assertions.assertThat(responseEntity).isNotNull();
		
		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		
		Assertions.assertThat(responseEntity.getBody()).isNull();;
		
		
	}
	
	
	@Test
	@DisplayName("update save updated anime when successful")
	void update_SaveUpdatedAnime_WhenSuccessful() {
		
		ResponseEntity<Void> responseEntity = animeController.update(AnimeCreator.createValidAnime());
				
		Assertions.assertThat(responseEntity).isNotNull();
		
		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		
		Assertions.assertThat(responseEntity.getBody()).isNull();
		
		
	}
	
}
