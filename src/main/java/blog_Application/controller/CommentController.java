package blog_Application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import blog_Application.Paylaod.CommentDto;
import blog_Application.ServiceImpl.CommentServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@SecurityRequirement(name="scheme1")
@Tag(name="Comment Controller")
public class CommentController {
	
	@Autowired
	private CommentServiceImpl commentServiceImpl;
	
	@PostMapping("/user/{userid}/post/{postid}/comment")
	public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto ,@PathVariable long userid ,@PathVariable long postid)
	{
		return new ResponseEntity<CommentDto>(commentServiceImpl.createComment(commentDto,userid,postid),HttpStatus.CREATED);
	}
	
	@GetMapping("/comments")
	public ResponseEntity<List<CommentDto>>getAllComment(@RequestParam(name ="pageNumber" ,defaultValue = "0" ,required = false) int pageNumber ,
			 @RequestParam(name="pageSize",defaultValue = "3" ,required = false) int pageSize,
			 @RequestParam(name="sortBy" ,defaultValue = "commentid" ,required = false) String sortBy,
			 @RequestParam(name ="sortDir" ,defaultValue ="desc" ,required =false )String sortDir)
	{
		return new ResponseEntity<List<CommentDto>>(commentServiceImpl.getAllCommens(pageNumber, pageSize, sortBy, sortDir),HttpStatus.OK);
	}
	
	@GetMapping("/comment/{commentid}")
	public ResponseEntity<CommentDto> getOneComment(@PathVariable long commentid)
	{
		return new ResponseEntity<CommentDto>(commentServiceImpl.getOneComment(commentid),HttpStatus.OK);
	}
	
	@PutMapping("/comment/{commentid}")
	public ResponseEntity<CommentDto>updateComment(@RequestBody CommentDto commentDto ,@PathVariable long commentid)
	{
		return new ResponseEntity<CommentDto>(commentServiceImpl.updateComment(commentDto, commentid),HttpStatus.OK);
	}
	@DeleteMapping("/comment/{commentid}")
	public ResponseEntity<String> deleteComment(@PathVariable long commentid)
	{
		return  new ResponseEntity<String>(commentServiceImpl.deleteComment(commentid),HttpStatus.OK);
	}

}
