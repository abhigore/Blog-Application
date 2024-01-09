package blog_Application.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import blog_Application.Model.Post;
import blog_Application.Paylaod.PostDto;
import blog_Application.Paylaod.PostResponse;
import blog_Application.ServiceImpl.PostServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;


@RestController
@SecurityRequirement(name="scheme1")
@Tag(name ="Post Controller")
public class PostController {
	Logger logger =LoggerFactory.getLogger(PostController.class);
	@Autowired
	private PostServiceImpl postServiceImpl;
	
	@Value("${custom.file.location}")
	private String path;
	
	@PostMapping("/user/{userid}/category/{catid}/post")
	public ResponseEntity<PostDto> createPost(@RequestPart("postDto") PostDto postDto ,
			@PathVariable long userid ,@PathVariable long catid ,
			final @RequestParam("file") MultipartFile file) throws IOException
	{
		logger.info("enter in the create method of postcontroller");
		
		return new ResponseEntity<PostDto>(postServiceImpl.CreatePost(postDto, userid, catid ,file),HttpStatus.CREATED);	
	}
	
	@GetMapping("/post/{postid}")
	public ResponseEntity<PostDto>getOnePost(@PathVariable long postid)
	{
		return new ResponseEntity<PostDto>(postServiceImpl.getPost(postid),HttpStatus.OK);
	}
	
	@GetMapping("/posts")
	public ResponseEntity<PostResponse>getAll(
			@RequestParam (name = "pageNumber" ,defaultValue = "0" ,required = false) int pageNumber,
			@RequestParam(name="pageSize",defaultValue = "3" ,required =false) int pageSize,
			@RequestParam(name="sortBy" ,defaultValue ="postid" ,required = false) String sortBy,
			@RequestParam(name ="sortDir", defaultValue = "asc" ,required = false)String sortDir
			)
	{
		return new ResponseEntity<PostResponse>(postServiceImpl.getAll(pageNumber ,pageSize,sortBy,sortDir),HttpStatus.OK);
	}
	
	@GetMapping("/user/{userid}/posts")
	public ResponseEntity<List<PostDto>> getAllPostsByUser(@PathVariable long userid)
	{
		return new ResponseEntity<List<PostDto>>(postServiceImpl.getAllByUser(userid),HttpStatus.OK);
	}
	
	@GetMapping("/category/{catid}/posts")
	public ResponseEntity<List<PostDto>> getAllPostsByCategory(@PathVariable long catid)
	{
		return new ResponseEntity<List<PostDto>>(postServiceImpl.getAllByCategory(catid),HttpStatus.OK);
	}
	
	@DeleteMapping("/post/{postid}")
	public ResponseEntity<String> deletePost(@PathVariable long postid)
	{
		return new ResponseEntity<String>(postServiceImpl.postDelete(postid),HttpStatus.OK);
	}
	
	@PutMapping("/post/{postid}")
	public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto , @PathVariable long postid)
	{
		return new ResponseEntity<PostDto>(postServiceImpl.UpdatePost(postDto, postid),HttpStatus.OK);
	}
	
	@GetMapping("/post/search/{key}")
	public ResponseEntity<List<PostDto>>searchPostbytitle(@PathVariable String key)
	{
		return new ResponseEntity<List<PostDto>>(postServiceImpl.serachPosts(key),HttpStatus.OK);
	}
	
	@GetMapping("/post/searchs/{key}")
	public ResponseEntity<List<PostDto>>searchByImageandTitle(@PathVariable String key)
	{
		return new ResponseEntity<List<PostDto>>(postServiceImpl.serachByImageAndTitle(key),HttpStatus.OK);
	}

	@GetMapping(value ="/post/image/{imagename}",produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable String imagename ,HttpServletResponse response) throws IOException
	{
		logger.info("enter in the download method");
		InputStream resource = postServiceImpl.getResource(path, imagename);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		
		StreamUtils.copy(resource, response.getOutputStream());
	}

}
