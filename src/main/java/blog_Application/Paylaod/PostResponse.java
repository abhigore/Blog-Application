package blog_Application.Paylaod;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class PostResponse {
	
	private int pageNumber;
	
	private int pageSize;
	
	private long totalElement;
	
	private int totalPage;
	
	private boolean lastPage;
	
	private List<PostDto> content;
	

}
