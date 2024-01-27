package blog_Application.ServiceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import blog_Application.Exception.ResourceNotFoundException;
import blog_Application.Model.Category;
import blog_Application.Model.Post;
import blog_Application.Model.User;
import blog_Application.Paylaod.CategoryDto;
import blog_Application.Paylaod.PostDto;
import blog_Application.Paylaod.PostResponse;
import blog_Application.Paylaod.UserDto;
import blog_Application.Repository.CategoryRepo;
import blog_Application.Repository.PostRepo;
import blog_Application.Repository.UserRepo;
import blog_Application.Service.PostService;
import jakarta.persistence.PostRemove;

@Service("postServiceImpl")
public class PostServiceImpl implements PostService{

	@Autowired
	@Lazy
	private PostRepo postRepo;
	
	
	private ModelMapper mapper =new ModelMapper();
	
	@Autowired
	@Lazy
	private UserRepo userRepo;
	
	@Autowired
	@Lazy
	private CategoryRepo categoryRepo;
	
	
	
 
	
	Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);
	
	
	@Override
	public PostDto CreatePost(PostDto postDto, long userid, long catid ,MultipartFile file) throws IOException
	{
		
		
		FileUploading fileUploading = new FileUploading(); 
		Post post  =postDtoToPost(postDto);
		
		User user=userRepo.findById(userid).orElseThrow(()->new ResourceNotFoundException("User", "id", userid));
		
		
		Category category =categoryRepo.findByCatid(catid).orElseThrow(()->new ResourceNotFoundException("Category", "id", catid));
		
		post.setAddDate(new Date(System.currentTimeMillis()));
		
		
		String fileUpload = fileUploading.FileUpload(file);
	
		post.setImage(fileUpload);
		post.setUser(user);
		post.setCategory(category);
		postRepo.save(post);
		
		PostDto postDto2 = postToPostDto(post);

		UserDto userDto = mapper.map(post.getUser(), UserDto.class);
         CategoryDto categoryDto = mapper.map(post.getCategory(), CategoryDto.class);
		postDto2.setUserdto(userDto);
	    postDto2.setCategoryDto(categoryDto);
		return postDto2;
	}

	@Override
	public PostDto UpdatePost(PostDto postDto, long postid) {
		
		Post post =postRepo.findByPostid(postid).orElseThrow(()->new ResourceNotFoundException("Post", "post id", postid));
		
		if(postDto.getTitle()!=null)
		{
			post.setTitle(postDto.getTitle());
			
		}
		if(postDto.getContent()!=null)
		{
			post.setContent(postDto.getContent());
			
		}
		if(postDto.getImage()!=null)
		{
			post.setImage(postDto.getImage());
			
		}
		
		Post post2 = postRepo.saveAndFlush(post);
		
		return postToPostDto(post2);
	}

	@Override
	public PostDto getPost(long postid) {
		Post post = postRepo.findByPostid(postid).orElseThrow(()->new ResourceNotFoundException("Post", "id", postid));
		PostDto postDto = postToPostDto(post);
		return postDto;
	}

	@Override
	public PostResponse getAll(int pageNumber ,int pageSize, String sortBy,String sortDir) {
	
		Sort sort = (sortDir.equalsIgnoreCase("asc")?(Sort.by(sortBy).ascending()):(Sort.by(sortBy).descending()));
		Pageable r =PageRequest.of(pageNumber, pageSize,sort);
		
		
	 Page<Post> postPerPage = postRepo.findAll(r);
	 
	 List<Post>posts =postPerPage.getContent();
	 
	 List<PostDto>postdtos =posts.stream().map((post)->postToPostDto(post)).collect(Collectors.toList());
	  
	 PostResponse postResponse =new PostResponse();
	 
	 postResponse.setContent(postdtos);
	 postResponse.setPageNumber(postPerPage.getNumber());
	 postResponse.setPageSize(postPerPage.getSize());
	 postResponse.setTotalElement(postPerPage.getTotalElements());
	 postResponse.setTotalPage(postPerPage.getTotalPages());
	 postResponse.setLastPage(postPerPage.isLast());
	 
	 
	 return postResponse;
		
	}

	@Override
	public String postDelete(long postid) {
		Post post =postRepo.findByPostid(postid).orElseThrow(()->new ResourceNotFoundException("Post","post id", postid));
		postRepo.delete(post);
		return "Post deleted successfully :"  + postid;
	}

	@Override
	public List<PostDto> getAllByUser(long userid) {
	
		
		User user =userRepo.findById(userid).orElseThrow(()->new ResourceNotFoundException("user", "id", userid));
		List<Post> posts = postRepo.findByUser(user).orElseThrow(()->new ResourceNotFoundException("Posts", "User ig", userid));
	
		
		List<PostDto> list =posts.stream().
				map((post)->postToPostDto(post)).collect(Collectors.toList());
		return list;
	}

	@Override
	public List<PostDto> getAllByCategory(long catid) {
		Category catid1= categoryRepo.findByCatid(catid).orElseThrow(()->new ResourceNotFoundException("Category", "id", catid));
		List<Post>posts =postRepo.findByCategory(catid1).orElseThrow(()-> new ResourceNotFoundException("Posts", "category id", catid));
		
		List<PostDto> list =posts.stream().map((post)->this.mapper.map(post, PostDto.class)).collect(Collectors.toList());
		
		
		return list;
	}

	
	public Post postDtoToPost(PostDto postDto)
	{ 
				
		Post post =mapper.map(postDto, Post.class);
		return post;
	}
	
	public PostDto postToPostDto(Post post)
	{
		TypeMap<Post, PostDto> typemapper = this.mapper.typeMap(Post.class,PostDto.class)
		.addMapping(Post ::getPostid, PostDto::setPostid)
		.addMapping(Post ::getTitle, PostDto::setTitle)
		.addMapping(Post ::getAddDate, PostDto::setAddDate)
		.addMapping(Post ::getContent, PostDto::setContent)
		.addMapping(Post ::getImage, PostDto::setImage)
		.addMapping((sr)->sr.getUser().getId(), PostDto::setUserDtoId)
		.addMapping((sr)->sr.getUser().getName(), PostDto::setUserDtoName)
		.addMapping((sr)->sr.getUser().getEmail(), PostDto::setUserDtoEmail)
		.addMapping((sr)->sr.getUser().getAbout(), PostDto::setUserDtoAbout)
		.addMapping((sr)->sr.getUser().getPassword(), PostDto::setUserDtoPassword)
		.addMapping((sr)->sr.getCategory().getCatid(),PostDto::setCategoryDtoId)
		.addMapping((sr)->sr.getCategory().getCatname(),PostDto::setCategoryDtoName)
		.addMapping((sr)->sr.getCategory().getCatabout(),PostDto::setCategoryDtoAbout);
		
		PostDto postDto = typemapper.map(post);
		return postDto;
	}

	@Override
	public List<PostDto> serachPosts(String key) {
		List<Post>posts =postRepo.searchByTitle("%"+key+"%");
		List<PostDto>postdtos =posts.stream().map((post)->postToPostDto(post)).collect(Collectors.toList());
		return postdtos;
	}

	@Override
	public List<PostDto> serachByImageAndTitle(String key) {
	   List<Post>posts=postRepo.searchByTitleAndImage("%"+key+"%");
	   List<PostDto>postdtos =posts.stream().map((post)->postToPostDto(post)).collect(Collectors.toList());
		return postdtos;
	}

	@Override
	public InputStream getResource(String path, String filename) throws FileNotFoundException {
		 String image = path+File.separator+postRepo.getByImage(filename);
		 logger.info("the image path is : " +image);
		 InputStream is = new FileInputStream(image);
		 return is;
	}
	
	
	

	
}
