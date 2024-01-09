package blog_Application.Paylaod;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import blog_Application.Model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {
	
	
	private long id;
	
	@NotEmpty
	@Size(min=2,message = "Enter the name and minimun character must be 2 ")
	private String name;
	
	@Email
	@Pattern(regexp = "^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$" ,message = "Please enter Valid Email !!")
	private String email;
	@NotEmpty
	
	@Size(min=4 ,message = "Password length must be minimum 3 and maximum 12 ")
	private String password;
	
	@NotEmpty
	private String about;
	
	private Set<RoleDto>roles =new HashSet<>();
	
	
	

}
