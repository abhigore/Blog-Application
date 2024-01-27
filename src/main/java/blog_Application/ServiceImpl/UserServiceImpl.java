package blog_Application.ServiceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import blog_Application.AppConstants.AppConstants;
import blog_Application.Exception.ResourceNotFoundException;
import blog_Application.Model.Role;
import blog_Application.Model.User;
import blog_Application.Paylaod.RoleDto;
import blog_Application.Paylaod.UserDto;
import blog_Application.Repository.RoleRepo;
import blog_Application.Repository.UserRepo;
import blog_Application.Service.UserService;

@Service
public class UserServiceImpl implements UserService {
	

	@Autowired
	@Lazy
	private  UserRepo userRepo;
	
	@Autowired
	private ModelMapper mapper;


	@Autowired
	@Lazy
	private RoleRepo roleRepo;
	
	@Autowired
	@Lazy
	private RoleServiceImpl roleServiceImpl;
	
	Logger logger =LoggerFactory.getLogger(UserServiceImpl.class);

	
	@Override
	public UserDto create(UserDto userDto) {

		Role role =new Role();
		PasswordEncoder encoder=new BCryptPasswordEncoder();
		User user = userDtOToUser(userDto);
		String pass = encoder.encode(user.getPassword());
		user.setPassword(pass);
		
		logger.info("User password =" + pass);

		Optional<Role> role1 = roleRepo.findByRoleid(AppConstants.ROLE_USER);
		
		if(role1.isPresent())
		{
			 role =role1.get();
		}

		user.getRoles().add(role);

		logger.info("the enter role  is = " + user.getRoles().toString());
		User saveUser = userRepo.save(user);
		// long id =user.getId();

		UserDto userDto2 = userToDto(saveUser);
		Set<Role> roles = user.getRoles();
		Set<RoleDto> roledtos = roleToRoleDtoSet(roles);

		userDto2.setRoles(roledtos);
		return userDto2;
	}

	

	@Override
	public UserDto getUser(long id) {
		User user = userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("User" ,"id" ,id));
	
		UserDto userDto = userToDto(user);
		
		return userDto;
	}

	@Override
	public List<User> getAllUser(int pageNumber ,int pageSize) {
	
		Pageable r =PageRequest.of(pageNumber, pageSize);
		
		
		 Page<User> userPerPage = userRepo.findAll(r);
		 List<User> list =userPerPage.getContent();
		 if(list.isEmpty())
		 {
			 logger.info("No User Entry Available");
		 }
		 return list;
	}

	@Override
	public UserDto Update(UserDto userDto, long id) {
		User user1 =userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("User" ,"id" ,id));
		if(user1 ==null)
		{
			 throw new ResourceNotFoundException("User", "id", id);
		}
		if(userDto.getName()!=null)
		{
			user1.setName(userDto.getName());
		}
		if(userDto.getEmail()!=null)
		{
			user1.setEmail(userDto.getEmail());
		}
		if(userDto.getPassword()!=null)
		{
			user1.setPassword(userDto.getPassword());
		}
		if(userDto.getAbout()!=null)
		{
			user1.setAbout(userDto.getAbout());
		}
		
	    userRepo.saveAndFlush(user1);

		UserDto userDto1 = userToDto(user1);
		return userDto1;
	}

	@Override
	public String Delete(long id) {
		User user =userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("User" ,"id" ,id));
		if(user == null)
		{
			logger.info("User not found : "+ id);
			return "User is not Found : " + id;	
		}
	    userRepo.delete(user);
		return "User Deleted Successfull : " + id ;
	}
	
public UserDto userToDto(User user) {
		
	//	UserDto userDto =mapper.map(user, UserDto.class);
	UserDto userDto =new UserDto();
		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setEmail(user.getEmail());
		userDto.setPassword(user.getPassword());
		userDto.setAbout(user.getAbout());
		
	//	userDto.setRoles(this.roleToRoleDtoSet(user.getRoles()));
		return userDto;
	
	}

    public Set<RoleDto> roleToRoleDtoSet(Set<Role> roles)
    {
    	Set<RoleDto> roledtos =new LinkedHashSet();
    	
    	for(Role role : roles)
    	{
    	
    		RoleDto roleDto =new RoleDto();
    		roleDto.setRoleid(role.getRoleid());
    		roleDto.setRolename(role.getRolename());
    		
    		roledtos.add(roleDto);
    		
    		
    	}
    	return roledtos;
    }

     public User userDtOToUser(UserDto userDto)
     {
    	// User user = mapper.map(userDto,User.class);
    	 User user =new User();
    	// user.setId(userDto.getId());
    	 user.setName(userDto.getName());
    	 user.setEmail(userDto.getEmail());
    	 user.setPassword(userDto.getPassword());
    	 user.setAbout(userDto.getAbout());
    	 return user;
     }

}
