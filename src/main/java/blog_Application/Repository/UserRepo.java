package blog_Application.Repository;

import java.util.List;
import java.util.Optional;

import org.hibernate.annotations.JoinColumnOrFormula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import blog_Application.Model.User;
import blog_Application.Paylaod.UserDto;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{

	public Optional<User> findById(long id);
	Optional<User>findByEmail(String email);

	@Query("SELECT u FROM User u JOIN u.roles r WHERE r.roleid = :key")
	public List<User> getAllUserByRole(@Param("key") long roleid);
}
