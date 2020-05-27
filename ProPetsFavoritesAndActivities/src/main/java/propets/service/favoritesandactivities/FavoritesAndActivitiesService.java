package propets.service.favoritesandactivities;

import propets.dto.favoritesandactivities.ResponsePostsDTO;

public interface FavoritesAndActivitiesService {
	
	ResponsePostsDTO getUserPosts(String login, boolean favoritesOrActivities);// true = "Favorites", false = "Activities"
	
}
