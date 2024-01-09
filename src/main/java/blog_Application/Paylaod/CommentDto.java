package blog_Application.Paylaod;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
	
	private long commentid;
	
	private String comment;
	
	private PostDto postDto;
	
	private UserDto userDto;
	
	
	

	
}
