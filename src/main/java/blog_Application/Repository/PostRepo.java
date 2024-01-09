package blog_Application.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import blog_Application.Model.Category;
import blog_Application.Model.Post;
import blog_Application.Model.User;
import blog_Application.Paylaod.PostDto;

public interface PostRepo extends JpaRepository<Post, Long> {

	Optional<Post> findByPostid(long postid);
	Optional<List<Post>> findByUser(User user);
	Optional<List<Post>> findByCategory(Category category);
	
	@Query("select p from Post p where p.title like :key")
	public List<Post>searchByTitle(@Param("key") String keyword);
	
	@Query("select p from Post p where p.title like:key OR p.image like:key")
	public List<Post>searchByTitleAndImage(@Param("key") String key);
	
	@Query("select p.image from Post p where p.image=:filename")
	public String getByImage(@Param("filename")String filename);
	
}
