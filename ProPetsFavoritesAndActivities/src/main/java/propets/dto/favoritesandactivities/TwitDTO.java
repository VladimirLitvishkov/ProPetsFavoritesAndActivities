package propets.dto.favoritesandactivities;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EqualsAndHashCode(of = { "id" })
public class TwitDTO {
	String id;
	String userLogin;
	String userName;
	String avatar;
	String text;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime datePost;
	@Singular("images")
	List<String> images;

}
