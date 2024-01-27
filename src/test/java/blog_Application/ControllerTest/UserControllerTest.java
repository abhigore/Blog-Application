package blog_Application.ControllerTest;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;


import blog_Application.AppConstants.AppConstants;
import blog_Application.Model.Role;
import blog_Application.Model.User;
import blog_Application.Myconfig.SecurityConfig;
import blog_Application.Paylaod.RoleDto;
import blog_Application.Paylaod.UserDto;
import blog_Application.Repository.RoleRepo;
import blog_Application.ServiceImpl.UserServiceImpl;

import blog_Application.controller.UserController;

@WebMvcTest(value = UserController.class)
@TestInstance(Lifecycle.PER_CLASS)
//@ComponentScan(basePackages = {"blog_Application.ServiceImpl","blog_Application.Repository.RoleRepo","blog_Application.controller"})
public class UserControllerTest {
	
	
    @MockBean
    private RoleRepo roleRepo;
    
	@MockBean
	private UserServiceImpl userServiceImpl;

	private PasswordEncoder encode =new BCryptPasswordEncoder();

    @Autowired
	private MockMvc mockMvc;
    
    private ModelMapper mapper =new ModelMapper();
	
	private UserDto userDto =new UserDto(1L,"Vaibhav Limkar" ,"vaibhav@gmail.com","1234","this is only testing purpose");
	
    private long userid =1;
    private  ObjectMapper objMapper =new ObjectMapper();
   
    private ObjectWriter objWriter =objMapper.writer();
 
    
    Logger logger =LoggerFactory.getLogger(UserControllerTest.class);
    
    @Test
    @DisplayName("Create User from UserController")
    public void createUserTest() throws Exception
    {
    	
    	Role role = new Role();
		when(roleRepo.findByRoleid(AppConstants.ROLE_USER)).thenReturn(java.util.Optional.of(role));
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		Set<RoleDto>roleDtos =userServiceImpl.roleToRoleDtoSet(roles);
		userDto.setRoles(roleDtos);
    	String password =encode.encode("1234");
    	userDto.setPassword(password);
    	
    	logger.info("hashcode for userDto +++ = " + userDto.hashCode());
    	//Set POur Mock Implementation 
    	when(userServiceImpl.create(any(UserDto.class))).thenReturn(userDto);
    
    	logger.info("Enter in the Test Controller Class");
    	//Execute the Post Request
    	mockMvc.perform(post("/user")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(objWriter.writeValueAsString(userDto)))
    	
    	//Check the Content Type and Content
    	.andExpect(content().contentType(MediaType.APPLICATION_JSON))
    	.andExpect(status().is(200))
    	
    	
    	.andExpect(jsonPath("$.id", is(1)))
    	.andExpect( jsonPath("$.name" ,is("Vaibhav Limkar")))
    	.andExpect(jsonPath("$.email" ,  is("vaibhav@gmail.com")))
    	.andExpect(jsonPath("$.about" ,is("this is only testing purpose")));

    }
    
    @Test
    @DisplayName("Get One User from UserContollr")
   public void getOneUserTest() throws Exception
   {
    
	  User user =this.userServiceImpl.userDtOToUser(userDto);
    	when(userServiceImpl.getUser(userid)).thenReturn(userDto);
    	
    	mockMvc.perform(get("/user/{userid}",userid))
    	
    	.andExpect(content().contentType(MediaType.APPLICATION_JSON))
    	.andExpect(status().is(200))
    	
    	.andExpect(jsonPath("$.id",is(1)))
    	.andExpect(jsonPath("$.name",is("Vaibhav Limkar")))
    	.andExpect(jsonPath("$.email", is("vaibhav@gmail.com")))
    	.andExpect(jsonPath("$.about", is("this is only testing purpose")));
   }
   
    @Test
    @DisplayName("Get All User From Cntroller")
    public void getAllUserTest() throws Exception
    {
    	int pageNumber=0;
    	int pageSize =1;
    	User user =mapper.map(userDto,User.class);
    	//User user =this.userServiceImpl.userDtOToUser(userDto);
    	Role role = new Role();
		when(roleRepo.findByRoleid(AppConstants.ROLE_USER)).thenReturn(java.util.Optional.of(role));
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		user.setRoles(roles);
		List<User> users =new ArrayList<>();
		users.add(user);
		
		logger.info("user from the userController test = " +users);
    	
    	when(userServiceImpl.getAllUser(pageNumber, pageSize)).thenReturn(users);
    	
    	mockMvc.perform(get("/users")
    			.accept(MediaType.APPLICATION_JSON)
    			)
    	.andExpect(content().contentType(MediaType.APPLICATION_JSON))
    	.andExpect(status().is(200))
    	
    	
    	.andExpect(jsonPath("$[0].id", is(1)))  // Access the 'id' of the first user in the array
        .andExpect(jsonPath("$[0].name", is("Vaibhav Limkar")))  // Additional assertions based on your User model
        .andExpect(jsonPath("$[0].email", is("vaibhav@gmail.com")))
        .andExpect(jsonPath("$[0].about", is("this is only testing purpose")));
    	
    }
    
    
    
    
}