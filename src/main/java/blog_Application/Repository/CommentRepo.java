package blog_Application.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import blog_Application.Model.Comment;

@Repository
public interface CommentRepo extends  JpaRepository<Comment, Long>{
	
	public Optional<Comment> findByCommentid(long commentid);

}
