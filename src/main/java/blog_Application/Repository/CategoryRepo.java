package blog_Application.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import blog_Application.Model.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long>{
	
	Optional<Category> findByCatid(long catid);

}
