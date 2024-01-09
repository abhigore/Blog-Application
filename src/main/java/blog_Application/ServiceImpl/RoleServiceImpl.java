package blog_Application.ServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog_Application.Exception.ResourceNotFoundException;
import blog_Application.Model.Role;
import blog_Application.Model.User;
import blog_Application.Paylaod.RoleDto;
import blog_Application.Paylaod.UserDto;
import blog_Application.Repository.RoleRepo;
import blog_Application.Repository.UserRepo;
import blog_Application.Service.RoleService;

@Service
public class RoleServiceImpl implements RoleService{

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private  RoleRepo roleRepo;

	
	@Autowired
	private UserRepo userRepo;
	
	
	@Override
	public RoleDto roleCreate(RoleDto roleDto ,long userid) {
	
		
		Set<User> users =new HashSet();
		Set<Role> roles =new HashSet();
		User user = this.userRepo.findById(userid).orElseThrow(()->new ResourceNotFoundException("User","user id ", userid));
		Role role = this.roleDtoTORole(roleDto);
		users.add(user);
		role.setUsers(users);
		Set<UserDto> userDtoSet = this.userToUserDtoSet(users);
		Role saverole = roleRepo.save(role);
		roles.add(saverole);
		user.setRoles(roles);
		userRepo.saveAndFlush(user);
		
		RoleDto roleDto2 = this.roleToRoleDto(saverole);
		
		
		
		roleDto2.setUserdtos(userDtoSet);
		return roleDto2;
	}

	@Override
	public RoleDto getOneRole(long roleid) {
		
		Role role = roleRepo.findByRoleid(roleid).orElseThrow(()->new ResourceNotFoundException("Role","role id", roleid));
		
		RoleDto roleDto = this.roleToRoleDto(role);
		return roleDto;
	}

	@Override
	public List<RoleDto> getAllRoles() {
		List<Role> roles = roleRepo.findAll();
		List<RoleDto>roledtos=roles.stream().map((role)->this.roleToRoleDto(role)).collect(Collectors.toList());
		return roledtos;
	}

	@Override
	public RoleDto updateRole(RoleDto roleDto, long roleid) {
		Role role = roleRepo.findByRoleid(roleid).orElseThrow(()->new ResourceNotFoundException("Role","role id ", roleid));
	   if(roleDto.getRolename()!=null)
	   {
		   role.setRolename(roleDto.getRolename());
	   }
	   Role saveRole = roleRepo.saveAndFlush(role);
	   RoleDto roledto = this.roleToRoleDto(saveRole);
		return roledto;
	}

	@Override
	public String deleteRole(long roleid) {
		Role role = roleRepo.findByRoleid(roleid).orElseThrow(()->new ResourceNotFoundException("Role", "role id", roleid));
		
		roleRepo.delete(role);
		return "Role deleted successfully : " + roleid;
	}
	
	public Role roleDtoTORole(RoleDto roleDto)
	{
		Role role = mapper.map(roleDto, Role.class);
		return role;
	}
	
	
	public String updateUserRole(RoleDto roleDto ,long userid ,long roleid)
	{
		User user = userRepo.findById(userid).orElseThrow(()->new ResourceNotFoundException("User", "user id", userid));
		Role role = roleRepo.findByRoleid(roleid).orElseThrow(()->new ResourceNotFoundException("Role", "role id", roleid));
		
		user.getRoles().add(role);
		userRepo.saveAndFlush(user);
		
		
		
		return "Role Added Successfully with " + "user id :" +user.getId() +"  with role name : " + role.getRolename();
	}
	
	
	
	public RoleDto roleToRoleDto(Role role)
	{
		
		TypeMap<Role, RoleDto> typemap = mapper.typeMap(Role.class, RoleDto.class)
		.addMapping(Role:: getRoleid,RoleDto :: setRoleid)
		.addMapping(Role:: getRolename,RoleDto ::setUserdtos)
		.addMapping((sc)->sc.getUsers(),(des, value) ->des.setUserdtos(this.userToUserDtoSet(role.getUsers())));
		
		 RoleDto roleDto = typemap.map(role);
	//	RoleDto roleDto = mapper.map(role, RoleDto.class);
		return roleDto;
		
	}
	
	public Set<UserDto> userToUserDtoSet(Set<User> users)
	{
		Set<UserDto> userdtos =new HashSet<>();
		
		for( User user : users)
		{
			UserDto userDto = mapper.map(user,UserDto.class);
			userdtos.add(userDto);
		}
		return userdtos;
	}

	
	@Override
	public List<User> getAllUserByRole(long roleid) {
		List<User> users = userRepo.getAllUserByRole(roleid);
	//	List<UserDto> userdtos =users.stream().map((user)->impl.userToDto(user)).collect(Collectors.toList());
		return users;
	}
	
	
	
}
