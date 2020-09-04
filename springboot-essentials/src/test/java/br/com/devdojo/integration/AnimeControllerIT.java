package br.com.devdojo.integration;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import br.com.devdojo.domain.Anime;
import br.com.devdojo.repository.AnimeRepository;
import br.com.devdojo.util.AnimeCreator;
import br.com.devdojo.wrapper.PageableResponse;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AnimeControllerIT {

	
	@Autowired
	@Qualifier(value = "testRestTemplateRoleUser")
	private TestRestTemplate testRestTemplateUser;	
	
	@Autowired
	@Qualifier(value = "testRestTemplateRoleAdmin")
	private TestRestTemplate testRestTemplateAdmin;
	
	@LocalServerPort
	private int port;
	
	@MockBean
	private AnimeRepository animeRepositoryMock;
	
	@Lazy
	@TestConfiguration
	static class Config {
		@Bean(name = "testRestTemplateRoleUser")
		public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
			RestTemplateBuilder restTemplateBuilder =  new RestTemplateBuilder()
			.rootUri("http://localhost:" + port)
			.basicAuthentication("devdojo", "academy");			
			
			return new TestRestTemplate(restTemplateBuilder);
		}
		@Bean(name = "testRestTemplateRoleAdmin")
		public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port) {
			RestTemplateBuilder restTemplateBuilder =  new RestTemplateBuilder()
			.rootUri("http://localhost:" + port)
			.basicAuthentication("juliocp", "academy");			
			
			return new TestRestTemplate(restTemplateBuilder);
		}
	}
	

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

	
	}
	
	
	@Test
	@DisplayName("listAll returns a pageable list of animes when successful")
	void listAll_ReturnListOfAnimesInsidePageObject_WhenSuccessful() {
		
		String expectedName = AnimeCreator.createValidAnime().getName();
		Page<Anime> animePage = testRestTemplateUser.exchange("/animes", HttpMethod.GET,null, new ParameterizedTypeReference<PageableResponse<Anime>>() {}).getBody();
		
		Assertions.assertThat(animePage).isNotNull();
		
		Assertions.assertThat(animePage.toList()).isNotEmpty();
		
		Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
		
		
	}

	@Test
	@DisplayName("findById returns an anime when successful")
	void findById_ReturnAnAnime_WhenSuccessful() {
		
		Integer expectedId = AnimeCreator.createValidAnime().getId();
		
		Anime anime = testRestTemplateUser.getForObject("/animes/1", Anime.class);
		
		Assertions.assertThat(anime).isNotNull();
		
		Assertions.assertThat(anime.getId()).isNotNull();
		
		Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
		
		
	}
	
	
	@Test
	@DisplayName("findByName returns a  list of animes when successful")
	void findByName_ReturnListOfAnimes_WhenSuccessful() {
		
		String expectedName = AnimeCreator.createValidAnime().getName();
		
		List<Anime> animeList = testRestTemplateUser.exchange("/animes/find?name='tensei'",HttpMethod.GET,null, new ParameterizedTypeReference<List<Anime>>(){}).getBody();
			
		Assertions.assertThat(animeList).isNotNull().isNotEmpty();
		
		Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
		
		
	}
	

	@Test
	@DisplayName("save creates a anime when successful")
	void save_CreatesAnAnime_WhenSuccessful() {
		
		Integer expectedId = AnimeCreator.createValidAnime().getId();
		
		Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
		
		Anime anime = testRestTemplateUser.exchange("/animes", HttpMethod.POST, createJsonHttpEntity(animeToBeSaved), Anime.class).getBody();
		
		Assertions.assertThat(anime).isNotNull();
		
		Assertions.assertThat(anime.getId()).isNotNull();
		
		Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
		
		
	}
	
	
	@Test
	@DisplayName("delete removes the anime when successful")
	void delete_removesAnime_WhenSuccessful() {
		

		ResponseEntity<Void > responseEntity = testRestTemplateAdmin.exchange("/animes/admin/1", HttpMethod.DELETE, null, Void.class);
		
		Assertions.assertThat(responseEntity).isNotNull();
		
		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		
		Assertions.assertThat(responseEntity.getBody()).isNull();;
		
		
	}
	
	@Test
	@DisplayName("delete is forbidden when user is not admin")
	void delete_Return403_WhenUserIsNotAdmin() {
		
		ResponseEntity<Void > responseEntity = testRestTemplateUser.exchange("/animes/admin/1", HttpMethod.DELETE, null, Void.class);
		
		Assertions.assertThat(responseEntity).isNotNull();
		
		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		
		
	}
	@Test
	@DisplayName("update save updated anime when successful")
	void update_SaveUpdatedAnime_WhenSuccessful() {
		
		Anime validAnime = AnimeCreator.createValidAnime();
		
		ResponseEntity<Void> responseEntity = testRestTemplateUser.exchange("/animes", HttpMethod.PUT, createJsonHttpEntity(validAnime), Void.class);
				
		Assertions.assertThat(responseEntity).isNotNull();
		
		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		
		Assertions.assertThat(responseEntity.getBody()).isNull();
		
		
	}
	
	
	private HttpEntity<Anime> createJsonHttpEntity(Anime anime){
		return new HttpEntity<>(anime, createJsonHeader());
	}
	
	private static HttpHeaders createJsonHeader() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return httpHeaders;
	}

	
	
}
