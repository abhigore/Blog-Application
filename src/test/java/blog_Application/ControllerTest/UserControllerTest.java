package blog_Application.ControllerTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;


import blog_Application.AppConstants.AppConstants;
import blog_Application.Model.JwtAuthRequest;
import blog_Application.Model.Role;
import blog_Application.Model.User;
import blog_Application.Myconfig.SecurityConfig;
import blog_Application.Paylaod.RoleDto;
import blog_Application.Paylaod.UserDto;
import blog_Application.Repository.RoleRepo;
import blog_Application.ServiceImpl.UserServiceImpl;

import blog_Application.controller.UserController;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.bytebuddy.NamingStrategy.Suffixing.BaseNameResolver.ForGivenType;

@WebMvcTest(value = UserController.class)
@TestInstance(Lifecycle.PER_CLASS)
//@ComponentScan(basePackages = {"blog_Application.ServiceImpl","blog_Application.Repository.RoleRepo","blog_Application.controller"})
public class UserControllerTest {
	
	
    @MockBean
    private RoleRepo roleRepo;
    
	@MockBean
	private UserServiceImpl userServiceImpl;

	private  String secreteKey ="jwtToken";
	private PasswordEncoder encode =new BCryptPasswordEncoder();

    @Autowired
	private MockMvc mockMvc;
    private String token ="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2YWliaGF2MTIzQGdtYWlsLmNvbSIsImV4cCI6MTcwNjc5ODQyMywiaWF0IjoxNzA2NDM4NDIzfQ.Ywkq42wuWMioxkfQmwI2NOX72lPM1sSoDJAJTlw-M5y2gRjGqnNmeRK7XUmWYk-KThKS-Y2i3IXvvXEjUosebA";
    
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
    	.andDo(print())
    	
    	
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
    	.andDo(print())
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
    	
    	List<User> allUser = this.userServiceImpl.getAllUser(pageNumber, pageSize);
    	
    	mockMvc.perform(get("/users")
    			.accept(MediaType.APPLICATION_JSON)
    			)
    	.andExpect(content().contentType(MediaType.APPLICATION_JSON))
    	.andExpect(status().is(200))
    	.andDo(print());
    	
    	assertThat(allUser.size()).isEqualTo(1);
    	assertThat(allUser.get(0).getId()).isEqualTo(user.getId());
    	assertThat(allUser.get(0).getName()).isEqualTo(user.getName());
  
    }
    
  @Test
  @DisplayName("Update User from UserController ")
  public void updateUserTest() throws JsonProcessingException, Exception
  {
	  
	  UserDto userDto2 =new UserDto(1L,"Vaibhav Limkar" ,"vaibhav123@gmail.com","1234","this is only testing purpose");
	  //UserDto userDto3 =new UserDto(1,"Soham Sakhare" ,"soham@gmail.com","1234","this is only testing purpose");
	  UserDto userDto1 =new UserDto();
	  userDto1.setName("Soham Sakhare");
	  userDto1.setEmail("soham@gmail.com");
	  String username =userDto1.getEmail();
	  Map<String, String> claims = new HashMap<>();

	 // claims.put("password", "12345");
	  claims.put("rolename" ,"ROLE_ADMIN");
	  
	 // JwtAuthRequest  jwtAuthRequest =new JwtAuthRequest(username, "12345");
	  
	  userDto2.setName(userDto1.getName());
	  userDto2.setEmail(userDto1.getEmail());
	
	  when(userServiceImpl.Update(userDto1, userid)).thenReturn(userDto2);
	  
	  mockMvc.perform(put("/user/{userid}",userid)
			  //.header("Authorization" ,"Bearer "+token)
			  .contentType(MediaType.APPLICATION_JSON)
			  .content(objWriter.writeValueAsBytes(userDto2)))
			  
			//  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			  .andDo(print())

			  .andExpect(status().isUnauthorized());
//			  .andExpect(jsonPath("$.id",is(1)))
//			  .andExpect(jsonPath("$.name", is(userDto1.getName())))
//			  .andExpect(jsonPath("$.email", is(userDto1.getEmail())));
	  
	  
  }
    
  
  public String  generateToken(String username,Map<String ,String> claims)
  {
	
	
	  Date explirationTime =new Date( System.currentTimeMillis()+ 100*60*60);
	  
	Claims claim =Jwts.claims();
	claim.putAll(claims);
	
	logger.info(" rolename  from the userController test=" +claim.get("rolename"));
	 
	  
	   String token = Jwts
			  .builder()
			  .setSubject(username)
			 // .setClaims(claim)
			  .setExpiration(explirationTime)
			  .signWith(SignatureAlgorithm.HS512, secreteKey)
			  .compact();
	  
	 //  logger.info("Username from generated token from the User Controllre Test class = " + token);
	   getname();
	   boolean containsValue = Jwts.parser().setSigningKey(secreteKey).parseClaimsJws(token).getBody().containsValue(claims. keySet());
	   logger.info(" keySet fron  token from the User Controllre Test class = " + containsValue);
	   
	   return token;
	   }
    
  public String getname()
  {
	  String name =Jwts.parser().setSigningKey(secreteKey).parseClaimsJws(token).getBody().getSubject();
	
	  logger.info("Ussername from the token fechted by UserController Test class = " + name);
	  return name;
  }
    
  
  @Test
  @DisplayName("Detele User from UserController Test")
  public void deleteUserTest() throws Exception
  {
	  String message ="User Deleted Successfull : " + userid;
	  when(userServiceImpl.Delete(userid)).thenReturn(message);
	  
	 mockMvc.perform(delete("/user/{userid}" ,userid))
	 .andDo(print())
	 .andExpect(status().isUnauthorized());
	 
	  
  }
    
}