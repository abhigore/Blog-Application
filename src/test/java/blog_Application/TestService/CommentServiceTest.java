package blog_Application.TestService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import blog_Application.Model.Comment;
import blog_Application.Model.Post;
import blog_Application.Model.User;
import blog_Application.Paylaod.CommentDto;
import blog_Application.Repository.CommentRepo;
import blog_Application.Repository.PostRepo;
import blog_Application.Repository.UserRepo;
import blog_Application.ServiceImpl.CommentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

	@Mock
	private CommentRepo commentRepo;
	@Mock
	private PostRepo postRepo;

	@Mock
	private UserRepo userRepo;

	@InjectMocks
	private CommentServiceImpl commentService;

	private ModelMapper mapper = new ModelMapper();

	Logger logger = LoggerFactory.getLogger(CommentServiceTest.class);

	@Test
	public void createCommmentTest() {
		long postid = 1L;

		long userId = 2L;

		long commentid = 0L;
		User user = new User();
		user.setId(userId);
		when(userRepo.findById(userId)).thenReturn(java.util.Optional.of(user));

		Post post = new Post();
		post.setPostid(postid);
		when(postRepo.findByPostid(postid)).thenReturn(java.util.Optional.of(post));

		CommentDto commentDto = new CommentDto();
		commentDto.setCommentid(commentid);
		commentDto.setComment("this is the comment session");
		Comment comment = mapper.map(commentDto, Comment.class);
		comment.setUser(user);
		comment.setPost(post);

		when(commentRepo.save(any(Comment.class))).thenReturn(comment);

		CommentDto expectedResult = commentService.commentToCommentDto(comment);

		logger.info("The expected_result of  create Command = " + expectedResult.getComment() + ", post id : "
				+ expectedResult.getPostDto().getPostid() + ", user id :" + expectedResult.getUserDto().getId());

		CommentDto result = commentService.createComment(commentDto, userId, postid);

		logger.info("The actual_result of  create Command = " + result.getComment() + ", post id :"
				+ result.getPostDto().getPostid() + " , user id :" + result.getUserDto().getId());

		assertEquals(expectedResult.getComment(), result.getComment());

		verify(userRepo).findById(userId);
		verify(postRepo).findByPostid(postid);
		logger.info("----------------------------------------------------------------------------------------");

	}

	@Test
	public void getOneCommentTest() {
		long commentid = 1L;
		CommentDto commentDto = new CommentDto();
		commentDto.setCommentid(commentid);
		commentDto.setComment("this is the comment session");
		Comment comment = mapper.map(commentDto, Comment.class);

		long userId = 2L;

		long postid = 0L;
		User user = new User();
		user.setId(userId);

		Post post = new Post();
		post.setPostid(postid);

		comment.setUser(user);
		comment.setPost(post);
		when(commentRepo.findByCommentid(commentid)).thenReturn(java.util.Optional.of(comment));

		CommentDto expectedResult = commentService.commentToCommentDto(comment);
		logger.info("the expected result of get one comment id = " + expectedResult.getCommentid());

		CommentDto result = commentService.getOneComment(commentid);
		logger.info("the actual result of get one comment id = " + result.getCommentid());

		assertEquals(expectedResult.getCommentid(), result.getCommentid());

		verify(commentRepo).findByCommentid(commentid);
		logger.info("----------------------------------------------------------------------------------------");

	}

	@Test
	public void getAllCommentsTest() {
		int pageNumber = 0;
		int pageSize = 1;
		String sortBy = "commentid";
		String sortDir = "desc";

		long commentid = 1L;
		CommentDto commentDto = new CommentDto();
		commentDto.setCommentid(commentid);
		commentDto.setComment("this is the comment session");
		Comment comment = mapper.map(commentDto, Comment.class);

		long userId = 2L;

		long postid = 0L;
		User user = new User();
		user.setId(userId);

		Post post = new Post();
		post.setPostid(postid);

		comment.setUser(user);
		comment.setPost(post);

		List<Comment> comments = new ArrayList<>();
		comments.add(comment);
		Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(Direction.DESC, sortBy) : Sort.by(Direction.ASC, sortBy);

		Pageable r = PageRequest.of(pageNumber, pageSize, sort);

		when(commentRepo.findAll(r)).thenReturn(new PageImpl<Comment>(comments));

		List<CommentDto> expectedResult = comments.stream()
				.map((comment1) -> commentService.commentToCommentDto(comment1)).collect(Collectors.toList());
		logger.info("the expected result of get All count of comments  = " + expectedResult.size());

		List<CommentDto> result = commentService.getAllCommens(pageNumber, pageSize, sortBy, sortDir);

		logger.info("the actual result of get All count of comments = " + result.size());

		assertEquals(expectedResult.size(), result.size());
		logger.info("----------------------------------------------------------------------------------------");
	}

	@Test
	public void updateCommentTest() {

		long commentid = 1L;
		CommentDto commentDto = new CommentDto();
		commentDto.setCommentid(commentid);
		commentDto.setComment("this is the comment session");
		Comment comment = mapper.map(commentDto, Comment.class);

		long userId = 2L;

		long postid = 0L;
		User user = new User();
		user.setId(userId);

		Post post = new Post();
		post.setPostid(postid);

		comment.setUser(user);
		comment.setPost(post);

		CommentDto commentDto1 = new CommentDto();

		commentDto1.setComment("This post is really beautiful");

		when(commentRepo.findByCommentid(commentid)).thenReturn(java.util.Optional.of(comment));
		if (commentDto1.getComment() != null) {
			comment.setComment(commentDto1.getComment());
		}

		when(commentRepo.saveAndFlush(comment)).thenReturn(comment);
		CommentDto expectedResult = commentService.commentToCommentDto(comment);

		logger.info("the expected result of update command = " + expectedResult.getComment());

		CommentDto result = commentService.updateComment(commentDto1, commentid);
		logger.info("the actual result of update command = " + result.getComment());

		assertEquals(expectedResult.getComment(), result.getComment());

		verify(commentRepo).findByCommentid(commentid);
		logger.info("----------------------------------------------------------------------------------------");

	}

	@Test
	public void deleteCommentTest() {
		long commentid = 1L;
		CommentDto commentDto = new CommentDto();
		commentDto.setCommentid(commentid);
		commentDto.setComment("this is the comment session");
		Comment comment = mapper.map(commentDto, Comment.class);

		when(commentRepo.findByCommentid(commentid)).thenReturn(java.util.Optional.of(comment));

		commentService.deleteComment(commentid);

		verify(commentRepo, times(1)).delete(comment);
		verify(commentRepo).findByCommentid(commentid);
		logger.info("----------------------------------------------------------------------------------------");
	}

}
