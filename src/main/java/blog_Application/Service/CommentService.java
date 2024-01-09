package blog_Application.Service;

import java.util.List;

import blog_Application.Paylaod.CommentDto;

public interface CommentService {

	public CommentDto createComment(CommentDto commentDto ,long userid , long postid);
	
	public List<CommentDto> getAllCommens(int pageNumber ,int pageSize,String sortBy ,String sortDir);
	
	public CommentDto getOneComment(long commentid);
	
	public CommentDto updateComment(CommentDto commentDto , long commentid);
	
	public String deleteComment(long commentid);


}
