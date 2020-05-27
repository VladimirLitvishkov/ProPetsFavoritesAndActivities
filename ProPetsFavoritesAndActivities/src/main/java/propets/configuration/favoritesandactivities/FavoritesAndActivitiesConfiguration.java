package propets.configuration.favoritesandactivities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@RefreshScope
@Getter
public class FavoritesAndActivitiesConfiguration {
	
	@Value("${urlAccounting}")
	String urlAccounting;
	
	@Value("${urlMessageingPosts}")
	String urlMessageingPosts;
	
	@Value("${urlLostAndFoundPosts}")
	String urlLostAndFoundPosts;
	
	@Value("${urlCheckToken}")
	String urlCheckToken;

}
