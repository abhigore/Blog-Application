package blog_Application.ServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import blog_Application.Exception.ResourceNotFoundException;
import blog_Application.Model.Comment;
import blog_Application.Model.Post;
import blog_Application.Model.User;
import blog_Application.Paylaod.CommentDto;
import blog_Application.Paylaod.PostDto;
import blog_Application.Repository.CommentRepo;
import blog_Application.Repository.PostRepo;
import blog_Application.Repository.UserRepo;
import blog_Application.Service.CommentService;


@Service
public class CommentServiceImpl  implements CommentService{

	@Autowired
	private CommentRepo commentRepo;

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private ModelMapper mapper;
    
	@Autowired
	private PostServiceImpl postServiceImpl;
	@Override
	public CommentDto createComment(CommentDto commentDto, long userid ,long postid) {
		 Post post = postRepo.findByPostid(postid).orElseThrow(()-> new ResourceNotFoundException("Post","post id ",postid));	
		 
		 User user = userRepo.findById(userid).orElseThrow(()->new ResourceNotFoundException("User", "Id ", userid));
		 
		 Comment comment = this.coomentDtoToComment(commentDto);
		 comment.setPost(post);
		 comment.setUser(user);
		 
		 Comment savedComment = commentRepo.save(comment);
		 
		 CommentDto commentDto1 = this.commentToCommentDto(savedComment);
		 
		return commentDto1;
	}

	@Override
	public List<CommentDto>getAllCommens(int pageNumber ,int pageSize,String sortBy ,String sortDir) {
		
		Sort sort =(sortDir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
		Pageable r =PageRequest.of(pageNumber, pageSize,sort);
	
		Page<Comment>commentPerPage=commentRepo.findAll(r);
		List<Comment> comments =commentPerPage.getContent();
		
		List<CommentDto> commentdtos= comments.stream().map((comment)->this.commentToCommentDto(comment)).collect(Collectors.toList());
		
		return commentdtos;
	}

	@Override
	public CommentDto getOneComment(long commentid) {
		Comment comment = commentRepo.findByCommentid(commentid).orElseThrow(()->new ResourceNotFoundException("Comment","comment id", commentid));
		CommentDto commentDto = this.commentToCommentDto(comment);
		return commentDto;
	}

	@Override
	public CommentDto updateComment(CommentDto commentDto, long commentid) {
		Comment comment =commentRepo.findByCommentid(commentid).orElseThrow(()->new ResourceNotFoundException("Comment","comment id", commentid));
		
		if(commentDto.getComment()!=null)
		{
			comment.setComment(commentDto.getComment());
		}
		Comment comment2= commentRepo.saveAndFlush(comment);
		CommentDto commentDto2 = this.commentToCommentDto(comment2);
		return commentDto2;
	}

	@Override
	public String deleteComment(long commentid) {
		Comment comment = commentRepo.findByCommentid(commentid).orElseThrow(()->new ResourceNotFoundException("Comment","comment id", commentid));
		commentRepo.delete(comment);
		return "Comment Deleted Successfully" + commentid;
	}
	
	public Comment coomentDtoToComment(CommentDto commentDto)
	{
		 Comment comment = mapper.map(commentDto,Comment.class);
		return  comment;	
	}
	
	public CommentDto commentToCommentDto(Comment comment)
	{
		PostDto postDto = postServiceImpl.postToPostDto(comment.getPost());
		
		TypeMap<Comment, CommentDto> typeMapCommentDto = mapper.typeMap(Comment.class, CommentDto.class)
		.addMapping(Comment::getCommentid, CommentDto :: setCommentid)
		.addMapping(Comment ::getComment, CommentDto :: setComment)
		.addMapping(Comment :: getUser, CommentDto :: setUserDto)
		.addMapping(Comment :: getPost,(dest, val) -> dest.setPostDto(postDto));
		
		 CommentDto commentDto = typeMapCommentDto.map(comment);
		return commentDto;
		
	}
	
}
