package blog_Application.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import blog_Application.Model.Category;

public interface CategoryRepo extends JpaRepository<Category, Long>{
	
	Optional<Category> findByCatid(long catid);

}
