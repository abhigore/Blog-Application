package blog_Application.Paylaod;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
	
	private long roleid;
	private String rolename;
	

	@JsonIgnore
	private Set<UserDto> userdtos =new HashSet<>();


}
