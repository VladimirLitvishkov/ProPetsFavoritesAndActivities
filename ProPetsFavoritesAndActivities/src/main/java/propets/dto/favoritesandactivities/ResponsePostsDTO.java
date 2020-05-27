package propets.dto.favoritesandactivities;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResponsePostsDTO {
	
	@Default
	Set<LostAndFoundDTO> lostAndFoundPosts = new HashSet<>();
	@Default
	Set<TwitDTO> twitPosts = new HashSet<>();

}
