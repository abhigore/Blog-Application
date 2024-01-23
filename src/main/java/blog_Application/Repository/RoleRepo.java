package blog_Application.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import blog_Application.Model.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role,Long>
{
	Optional<Role> findByRoleid(long id);
	

}
