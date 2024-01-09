package blog_Application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import blog_Application.Model.Role;
import blog_Application.Repository.RoleRepo;

@SpringBootApplication
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
	 	roleRepo.saveAndFlush(role);
	
      Role role1 =new Role();
		
		role1.setRoleid(2);
		role1.setRolename("ROLE_ADMIN"); 
	 	roleRepo.saveAndFlush(role1);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
