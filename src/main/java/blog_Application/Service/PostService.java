package blog_Application.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import blog_Application.Model.Post;
import blog_Application.Paylaod.PostDto;
import blog_Application.Paylaod.PostResponse;

public interface PostService {
	
	PostDto CreatePost(PostDto postDto ,long userid,long catid ,MultipartFile file) throws IOException;
	
	PostDto UpdatePost(PostDto postDto ,long postid);
	
	PostDto getPost(long postid);
	
	public PostResponse getAll(int pageNumber ,int pageSize,String sortBy ,String sortDir);
	
	String postDelete(long postid);
	
	List<PostDto> getAllByUser(long userid);
	
	List<PostDto> getAllByCategory(long catid);
	
	List<PostDto>serachPosts(String key);
	
	List<PostDto>serachByImageAndTitle(String key);
	
 	InputStream getResource(String path,String filename) throws FileNotFoundException;
	

}
