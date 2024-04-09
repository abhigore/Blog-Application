package blog_Application.controller;

import java.util.List;

import javax.swing.text.html.HTML;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import blog_Application.Exception.ResourceNotFoundException;
import blog_Application.Model.User;
import blog_Application.Paylaod.UserDto;
import blog_Application.Service.UserService;
import blog_Application.ServiceImpl.UserServiceImpl;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController

@SecurityScheme(
		
		name ="scheme1", type = SecuritySchemeType.HTTP,bearerFormat = "JWT" ,scheme = "bearer")
@OpenAPIDefinition(
info = @Info(
		title = "Blog Application",
		description = "BackEnd APIs for Blog Application",
		summary = "this the Banked application for Blog Application. Here we provides the api for fecting ,creating ,deleting the data",
		contact = @Contact(
				name = "Vaibhav Limkar",
				email = "vaibhavlimkar1607@gmail.com"
				),
		license = @License(
				name = "Apache 1.0"
				),
		version = "v2",
		termsOfService = "Terms And Condition"))
@SecurityRequirement(name="scheme1")
@Tag(name = "User Controller" ,description = "This is the user Controller")
public class UserController {
	
	@Autowired
	private UserServiceImpl userService;
	
	Logger logger =LoggerFactory.getLogger(UserController.class);
	
	@PostMapping("/user")
	public ResponseEntity<UserDto> create( @Valid @RequestBody UserDto userDto)
	{
		
		UserDto userDto1 = userService.create(userDto);
		if(userDto1 ==null)
		{
			logger.info("User is not created");
		}
		logger.info("user id from controller is =  " +userDto1.getId());
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body( userDto1);
	}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<UserDto> getUser(@PathVariable long id) 
	{
		UserDto user = userService.getUser(id);

		if(user!=null)
		{
		return new ResponseEntity<UserDto>(user,HttpStatus.OK);
		}
		else
		{
			 throw new ResourceNotFoundException("user", "id",id);
		}
	}
	
	@GetMapping(value = "/users" ,produces ="application/json")
	public ResponseEntity< List<User>>getAllUer(
			@RequestParam(name ="pageNumber" ,defaultValue = "0" ,required = false) int pageNumber,
			@RequestParam(name="pageSize" ,defaultValue = "2",required = false) int pageSize)
	{
		return new ResponseEntity<>(userService.getAllUser(pageNumber,pageSize),HttpStatus.OK);
		
	}
	
	@PutMapping(value="/user/{id}")
	public ResponseEntity<UserDto> update(@RequestBody UserDto userDto ,@PathVariable  long id)
	{
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(userService.Update(userDto, id));
	}
	
	@DeleteMapping("/user/{id}")
	public ResponseEntity<String> delete(@PathVariable long id)
	{
		return new ResponseEntity<String>(userService.Delete(id),HttpStatus.OK);
	}
	
	
	
}
