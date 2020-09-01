package br.com.devdojo.client;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import br.com.devdojo.domain.Anime;
import br.com.devdojo.wrapper.PageableResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpringClient {

	public static void main(String[] args) {
//		testGetWithRestTemplate();

		ResponseEntity<PageableResponse<Anime>> exchangeAnimeList = new RestTemplate().exchange(
				"http://localhost:8080/animes/", HttpMethod.GET, null,
				new ParameterizedTypeReference<PageableResponse<Anime>>() {
				});

		log.info("Anime list {}", exchangeAnimeList);

//		Anime overlord = Anime.builder().name("Overloard").build();
//		
//		Anime overlordSaved = new RestTemplate().postForObject("http://localhost:8080/animes", overlord, Anime.class);
//		
//		log.info("Overlord saved id: {}", overlordSaved.getId());

		Anime steinsGate = Anime.builder().name("Steins Gate").build();

		Anime stainsGateSaved = new RestTemplate().exchange("http://localhost:8080/animes", HttpMethod.POST,
				new HttpEntity<>(steinsGate, createJsonHeader()), Anime.class).getBody();

		log.info("Steins Gate saved id:d {}", stainsGateSaved.getId());

		stainsGateSaved.setName("Stains Gate Zero");
		ResponseEntity<Void> exchangeUpdated = new RestTemplate().exchange("http://localhost:8080/animes",
				HttpMethod.PUT, new HttpEntity<>(steinsGate, createJsonHeader()), Void.class);
		log.info("Steins Gate updates status {}", exchangeUpdated.getStatusCode());
		
		
		ResponseEntity<Void> exchangeDeleted = new RestTemplate()
				.exchange("http://localhost:8080/animes/{id}",HttpMethod.DELETE, null, Void.class, stainsGateSaved.getId());
		log.info("Steins Gate updates status {}", exchangeDeleted.getStatusCode());
	}

	private static HttpHeaders createJsonHeader() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return httpHeaders;
	}

	private static void testGetWithRestTemplate() {
		ResponseEntity<Anime> animeResponseEntoty = new RestTemplate().getForEntity("http://localhost:8080/animes/{id}",
				Anime.class, "1");

		log.info("Response Entity {}", animeResponseEntoty);

		log.info("Response Data {}", animeResponseEntoty.getBody());

		Anime anime = new RestTemplate().getForObject("http://localhost:8080/animes/{id}", Anime.class, "1");

		log.info("Anime {}", anime);
	}

}
