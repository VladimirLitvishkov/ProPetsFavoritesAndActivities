package propets.controller.favoritesandactivities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import propets.dto.favoritesandactivities.ResponsePostsDTO;
import propets.service.favoritesandactivities.FavoritesAndActivitiesService;

@RestController
@RequestMapping("/{lang}/v1")
@CrossOrigin(origins = "*")
public class FavoritesAndActivitiesController {
	
	@Autowired
	FavoritesAndActivitiesService service;
	
	@GetMapping("/{login:.*}/favorites")
	public ResponsePostsDTO getUserFavorites(@PathVariable String login) {
		return service.getUserPosts(login, true);
	}
	
	@GetMapping("/{login:.*}/activities")
	public ResponsePostsDTO getUserActivities(@PathVariable String login) {
		return service.getUserPosts(login, false);
	}

}
