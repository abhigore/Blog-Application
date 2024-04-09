package blog_Application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import blog_Application.Model.Role;
import blog_Application.Repository.RoleRepo;

@SpringBootApplication
@ComponentScan(basePackages = {"blog_Application.*","blog_Application.Repository","blog_Application.Security.UserDetails"})
public class BlogApplication {

	@Autowired
	private RoleRepo roleRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
		
	}
	
	public void run(String[]...s) throws Exception 
	{
		try
		{
		Role role =new Role();
		
		role.setRoleid(1);
		role.setRolename("ROLE_USER");
	 	roleRepo.save(role);
	
      Role role1 =new Role();
		
		role1.setRoleid(2);
		role1.setRolename("ROLE_ADMIN"); 
	 	roleRepo.save(role1);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
