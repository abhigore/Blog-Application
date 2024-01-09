package blog_Application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PutExchange;

import blog_Application.Model.User;
import blog_Application.Paylaod.RoleDto;
import blog_Application.Paylaod.UserDto;
import blog_Application.ServiceImpl.RoleServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@SecurityRequirement(name="scheme1")
@Tag(name ="Role Controller")
public class RoleController {
	
	@Autowired
	private RoleServiceImpl roleServiceImpl;
	
	@PostMapping("/user/{userid}/role")
	public ResponseEntity<RoleDto>createRole(@RequestBody  RoleDto roleDto ,@PathVariable long userid)
	{
		return new ResponseEntity<RoleDto>(roleServiceImpl.roleCreate(roleDto,userid),HttpStatus.CREATED);
	}
	
	@GetMapping("role/{roleid}")
	public ResponseEntity<RoleDto> getOneRole(@PathVariable long roleid)
	{
		return new ResponseEntity<RoleDto>(this.roleServiceImpl.getOneRole(roleid),HttpStatus.OK);
	}
	
	@GetMapping("/roles")
	public ResponseEntity<List<RoleDto>>getAllRoles()
	{
		return new ResponseEntity<List<RoleDto>>(this.roleServiceImpl.getAllRoles(),HttpStatus.OK);
	}
	
	@PutMapping("/role/{roleid}")
	public ResponseEntity<RoleDto>update(@RequestBody RoleDto roleDto , @PathVariable long roleid)
	{
		return new ResponseEntity<RoleDto>(this.roleServiceImpl.updateRole(roleDto, roleid),HttpStatus.OK);
	}
	
	@DeleteMapping("/role/{roleid}")
	public ResponseEntity<String>deleteRole(@PathVariable long roleid)
	{
		return new ResponseEntity<String>(this.roleServiceImpl.deleteRole(roleid),HttpStatus.OK);
	}
	
	@PutMapping("/user/{userid}/role/{roleid}")
	public ResponseEntity<String> updateUserRole(@RequestBody RoleDto roleDto , @PathVariable long userid , @PathVariable long roleid)
	{
		return new ResponseEntity<String>(roleServiceImpl.updateUserRole(roleDto, userid, roleid),HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/users/role/{roleid}")
	public ResponseEntity<List<User>>getAllUsersByRole(@PathVariable long roleid)
	{
		return new ResponseEntity<List<User>>(this.roleServiceImpl.getAllUserByRole(roleid),HttpStatus.OK);
	}

}
