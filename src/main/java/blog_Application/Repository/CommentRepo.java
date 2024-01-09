package blog_Application.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import blog_Application.Model.Comment;

public interface CommentRepo extends  JpaRepository<Comment, Long>{
	
	public Optional<Comment> findByCommentid(long commentid);

}
