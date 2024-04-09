package blog_Application.Service;

import java.util.List;

import blog_Application.Model.User;
import blog_Application.Paylaod.RoleDto;
import blog_Application.Paylaod.UserDto;

public interface RoleService {

	public RoleDto roleCreate(RoleDto roleDto,long userid); 
	public RoleDto getOneRole(long roleid);
	public List<RoleDto>getAllRoles();
	public RoleDto updateRole(RoleDto roleDto, long roleid);
	public String deleteRole(long roleid);
	
	public List<User>getAllUserByRole(long roleid); 
	
}
