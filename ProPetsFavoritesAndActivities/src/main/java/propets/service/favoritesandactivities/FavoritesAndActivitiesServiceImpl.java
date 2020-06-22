package propets.service.favoritesandactivities;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import propets.configuration.favoritesandactivities.FavoritesAndActivitiesConfiguration;
import propets.dto.favoritesandactivities.LostAndFoundDTO;
import propets.dto.favoritesandactivities.ResponsePostsDTO;
import propets.dto.favoritesandactivities.TwitDTO;

@Service
public class FavoritesAndActivitiesServiceImpl implements FavoritesAndActivitiesService {
	
	@Autowired
	FavoritesAndActivitiesConfiguration configuration;

	@Override // true = "Favorites", false = "Activities"
	public ResponsePostsDTO getUserPosts(String login, boolean favoritesOrActivities) {
		RestTemplate restTemplate = new RestTemplate();
		String typePosts = favoritesOrActivities ? "/favorites" : "/activities";
		String urlPostsId = configuration.getUrlAccounting() + login + typePosts;
		URI urlToAccountService = null;
		try {
			urlToAccountService = new URI(urlPostsId);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		RequestEntity<String> requestToAccountService = new RequestEntity<>(HttpMethod.GET, urlToAccountService);
		ResponseEntity<HashMap<String, Set<String>>> responseFromAccountService = restTemplate
				.exchange(requestToAccountService, new ParameterizedTypeReference<HashMap<String, Set<String>>>() {
				});
		HashMap<String, Set<String>> keys = responseFromAccountService.getBody();
		Set<TwitDTO> twits = null;
		Set<LostAndFoundDTO> lostAndFound = null;
		String urlLostAndFoundPosts = configuration.getUrlLostAndFoundPosts();
		String urlMessagePosts = configuration.getUrlMessageingPosts();
		for (String key : keys.keySet()) {
			if (key.equalsIgnoreCase(configuration.getMessageService())) {
				twits = requestToService(keys.get(key), urlMessagePosts/* , TwitDTO.class */);
			} else if (key.equalsIgnoreCase(configuration.getLostFoundService())) {
				lostAndFound = requestToService(keys.get(key), urlLostAndFoundPosts/* , LostAndFoundDTO.class */);
			}
//			switch (key) {
//			case "message":
//				twits = requestToService(keys.get(key), urlMessagePosts/* , TwitDTO.class */);
//				break;
//			case "lostfound":
//				lostAndFound = requestToService(keys.get(key), urlLostAndFoundPosts/* , LostAndFoundDTO.class */);
//				break;
//			default:
//				break;
//			}
		}

		return ResponsePostsDTO.builder().lostAndFoundPosts(lostAndFound).twitPosts(twits).build();
	}

	private <T> Set<T> requestToService(Set<String> postsId, String urlService) {
		RestTemplate restTemplate = new RestTemplate();
		URI urlToService = null;
		try {
			urlToService = new URI(urlService);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		RequestEntity<Set<String>> request = new RequestEntity<>(postsId, HttpMethod.POST, urlToService);
		ResponseEntity<Set<T>> response = restTemplate.exchange(request, new ParameterizedTypeReference<Set<T>>() {
		});
		return response.getBody();
	}

//	private <T> Set<T> requestToService(Set<String> postsId, String urlService, Class<T> classDTO) {
//		RestTemplate restTemplate = new RestTemplate();
//		Set<T> posts = new HashSet<>();
//		for (String id : postsId) {
//			URI urlToService = null;
//			try {
//				urlToService = new URI(urlService + "/" + id);
//			} catch (URISyntaxException e) {
//				e.printStackTrace();
//			}
//			RequestEntity<String> requestToService = new RequestEntity<String>(HttpMethod.GET, urlToService);
//			ResponseEntity<T> responseMessage = restTemplate.exchange(requestToService, classDTO);
//			posts.add(responseMessage.getBody());
//		}
//		return posts;
//	}

}
