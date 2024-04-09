package blog_Application.TestService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

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
import blog_Application.ServiceImpl.PostServiceImpl;

@ExtendWith(MockitoExtension.class)
public  class PostServiceTest {

	@Mock
	private PostRepo postRepo;

	@Mock
	private UserRepo userRepo;

	@Mock
	private CategoryRepo categoryRepo;

	@InjectMocks
	private PostServiceImpl postService;

	private ModelMapper mapper = new ModelMapper();

	Logger logger = LoggerFactory.getLogger(PostServiceTest.class);

	@Test
	public void createPostTest() throws IOException {

		MultipartFile file = new MockMultipartFile("example.jpg", "example.jpg", null, "hellow World!".getBytes());

		long userid = 1l;
		long catid = 2l;
		long postid = 3l;

		User user = new User();
		user.setId(userid);

		when(userRepo.findById(userid)).thenReturn(java.util.Optional.of(user));

		Category cat = new Category();
		cat.setCatid(catid);

		when(categoryRepo.findByCatid(catid)).thenReturn(java.util.Optional.of(cat));

		PostDto postDto = new PostDto();

		postDto.setPostid(postid);
		postDto.setTitle("new post");
		postDto.setContent("this post isonly testing purpose");
		postDto.setImage("text-file.txt");

		Post post = mapper.map(postDto, Post.class);

		post.setUser(user);
		post.setCategory(cat);
		post.setAddDate(new Date());

		when(postRepo.save(any(Post.class))).thenReturn(post);

		PostDto expectedResult = postService.postToPostDto(post);

		logger.info(" expeted userid and catid from Post Create Method = " + expectedResult.getUserdto().getId() + "  "
				+ expectedResult.getCategoryDto().getCatid());

		PostDto result = postService.CreatePost(postDto, userid, catid, file);

		logger.info(" actual userid and catid from Post Create Method = " + result.getUserdto().getId() + "  "
				+ result.getCategoryDto().getCatid());
		assertEquals(expectedResult.getPostid(), result.getPostid());
		assertEquals(expectedResult.getCategoryDto().getCatid(), result.getCategoryDto().getCatid());
		assertEquals(expectedResult.getUserdto().getId(), result.getUserdto().getId());
		logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

		// verify(userRepo).findById(userid);
		// verify(categoryRepo).findByCatid(catid);

	}

	@Test
	public void getOnePostTest() {

		MultipartFile file = new MockMultipartFile("example.jpg", "example.jpg", null, "hellow World!".getBytes());

		long userid = 1l;
		long catid = 2l;
		long postid = 3l;

		User user = new User();
		user.setId(userid);

		Category cat = new Category();
		cat.setCatid(catid);

		PostDto postDto = new PostDto();

		postDto.setPostid(postid);
		postDto.setTitle("new post");
		postDto.setContent("this post isonly testing purpose");
		postDto.setImage("text-file.txt");

		Post post = mapper.map(postDto, Post.class);

		post.setUser(user);
		post.setCategory(cat);
		post.setAddDate(new Date());

		when(postRepo.findByPostid(postid)).thenReturn(java.util.Optional.of(post));

		PostDto expectedResult = postService.postToPostDto(post);
		logger.info("expected result of getting  one post = " + expectedResult.toString());

		PostDto result = postService.getPost(postid);
		logger.info("Actual result of getting  one post = " + result.toString());

		verify(postRepo, times(1)).findByPostid(postid);

		assertEquals(expectedResult, result);
		logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	}

	@Test
	public void getAllPostsTest() {
		MultipartFile file = new MockMultipartFile("example.jpg", "example.jpg", null, "hellow World!".getBytes());

		long userid = 1l;
		long catid = 2l;
		long postid = 3l;
		int pageNumber = 0;
		int pageSize = 1;
		int totalpages = 1;
		int totalelement = 1;
		boolean lastpage = true;
		String SortBy = "postid";
		String SortDir = "desc";

		Sort sort = SortDir.equalsIgnoreCase("desc") ? Sort.by(Direction.DESC, "postid")
				: Sort.by(Direction.ASC, "postid");
		Pageable r = PageRequest.of(pageNumber, pageSize, sort);

		User user = new User();
		user.setId(userid);

		Category cat = new Category();
		cat.setCatid(catid);

		PostDto postDto = new PostDto();

		postDto.setPostid(postid);
		postDto.setTitle("new post");
		postDto.setContent("this post isonly testing purpose");
		postDto.setImage("text-file.txt");

		Post post = mapper.map(postDto, Post.class);

		post.setUser(user);
		post.setCategory(cat);
		post.setAddDate(new Date());

		List<Post> posts = new ArrayList<>();
		posts.add(post);

		when(postRepo.findAll(r)).thenReturn(new PageImpl<Post>(posts));

		List<PostDto> postDtos = posts.stream().map((post1) -> postService.postToPostDto(post1))
				.collect(Collectors.toList());

		PostResponse expectedResult = new PostResponse();
		expectedResult.setPageNumber(pageNumber);
		expectedResult.setPageSize(pageSize);
		expectedResult.setTotalElement(totalelement);
		expectedResult.setTotalPage(totalpages);
		expectedResult.setLastPage(lastpage);
		expectedResult.setContent(postDtos);

		logger.info("expected result of get All posts = " + expectedResult.getTotalElement());

		PostResponse result = postService.getAll(pageNumber, pageSize, SortBy, SortDir);

		logger.info("Actual result of get All posts = " + result.getTotalElement());
		assertEquals(expectedResult.getTotalElement(), result.getTotalElement());
		logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

	}

	@Test
	public void updatePostTest() {
		MultipartFile file = new MockMultipartFile("example.jpg", "example.jpg", null, "hellow World!".getBytes());

		long userid = 1l;
		long catid = 2l;
		long postid = 3l;

		User user = new User();
		user.setId(userid);

		Category cat = new Category();
		cat.setCatid(catid);

		PostDto postDto = new PostDto();

		postDto.setPostid(postid);
		postDto.setTitle("new post");
		postDto.setContent("this post is only testing purpose");
		postDto.setImage("text-file.txt");

		Post post = mapper.map(postDto, Post.class);

		post.setUser(user);
		post.setCategory(cat);
		post.setAddDate(new Date());

		PostDto postDto1 = new PostDto();
		postDto1.setTitle("Testing Post");

		when(postRepo.findByPostid(postid)).thenReturn(java.util.Optional.of(post));
		if (postDto1.getTitle() != null) {
			post.setTitle(postDto1.getTitle());

		}
		if (postDto1.getContent() != null) {
			post.setContent(postDto1.getContent());

		}
		if (postDto1.getImage() != null) {
			post.setImage(postDto1.getImage());

		}

		when(postRepo.saveAndFlush(post)).thenReturn(post);

		PostDto expectedResult = postService.postToPostDto(post);
		logger.info("Ecpected Result after updating title of post = " + expectedResult.getTitle());

		PostDto result = postService.UpdatePost(postDto1, postid);

		logger.info("Actual Result after updating title of post = " + result.getTitle());
		assertEquals(expectedResult, result);
		logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

	}

	@Test
	public void deletePostTest() {
		MultipartFile file = new MockMultipartFile("example.jpg", "example.jpg", null, "hellow World!".getBytes());

		long userid = 1l;
		long catid = 2l;
		long postid = 3l;

		User user = new User();
		user.setId(userid);

		Category cat = new Category();
		cat.setCatid(catid);

		PostDto postDto = new PostDto();

		postDto.setPostid(postid);
		postDto.setTitle("new post");
		postDto.setContent("this post is only testing purpose");
		postDto.setImage("text-file.txt");

		Post post = mapper.map(postDto, Post.class);

		post.setUser(user);
		post.setCategory(cat);
		post.setAddDate(new Date());

		when(postRepo.findByPostid(postid)).thenReturn(java.util.Optional.of(post));

		postService.postDelete(postid);

		verify(postRepo, times(1)).findByPostid(postid);
		verify(postRepo).delete(post);

	}

}
