package blog_Application.TestService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import TestConfig.MyTestConfig;
import blog_Application.AppConstants.AppConstants;

import blog_Application.Model.Role;
import blog_Application.Model.User;

import blog_Application.Paylaod.RoleDto;
import blog_Application.Paylaod.UserDto;
import blog_Application.Repository.RoleRepo;
import blog_Application.Repository.UserRepo;
import blog_Application.ServiceImpl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private UserRepo userRepo;

	@InjectMocks
	private UserServiceImpl serviceImpl;

	@Mock
	private RoleRepo roleRepo;

	@Mock
	private PasswordEncoder encoder;

	Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

	@Test
	public void createUserTest() {

		String EXPECTED_ENCODED_PASSWORD = "ecodededpasswoed";
		logger.info("Enter in the testing class");

		// PasswordEncoder encoder =new BCryptPasswordEncoder();

		// passing UserDto object as a input for Creating the User
		UserDto userDto = new UserDto();
		// userDto.setId(0);
		userDto.setName("vaibhav limkar");
		userDto.setEmail("vaibhav123@gmail.com");
		userDto.setPassword("12345");
		userDto.setAbout("i am java developer");

		// Original Result from Function

		// Mapping the userDto to user Class
		User user = serviceImpl.userDtOToUser(userDto);

		// Setting the External Input to the User that is Required and also given in the
		// Original function
		user.setPassword(EXPECTED_ENCODED_PASSWORD);

		Role role = new Role();
		when(roleRepo.findByRoleid(AppConstants.ROLE_USER)).thenReturn(java.util.Optional.of(role));
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		user.setRoles(roles);

		Mockito.when(userRepo.save(any(User.class))).thenReturn(user);

		UserDto expectedResult = serviceImpl.userToDto(user);
		Set<RoleDto> roleDtos = serviceImpl.roleToRoleDtoSet(roles);
		expectedResult.setRoles(roleDtos);

		UserDto result = serviceImpl.create(userDto);

		logger.info("reching at the last step");
		assertEquals(expectedResult.getId(), result.getId());
		logger.info("----------------------------------------------------------------------------------------");
	}

	@Test
	public void getOneUserTest() {

		long userid = 1L;
		UserDto userDto = new UserDto();
		userDto.setId(userid);
		userDto.setName("vaibhav limkar");
		userDto.setEmail("vaibhav123@gmail.com");
		userDto.setPassword("12345");
		userDto.setAbout("i am java developer");

		User user = serviceImpl.userDtOToUser(userDto);

		when(userRepo.findById(userid)).thenReturn(java.util.Optional.of(user));

		UserDto expectedResult = serviceImpl.userToDto(user);

		UserDto result = serviceImpl.getUser(userid);

		assertEquals(expectedResult, result);
		logger.info("----------------------------------------------------------------------------------------");

	}

	@Test
	public void getAllUsersTest() {
		long userid = 1L;
		UserDto userDto = new UserDto();
		userDto.setId(userid);
		userDto.setName("vaibhav limkar");
		userDto.setEmail("vaibhav123@gmail.com");
		userDto.setPassword("12345");
		userDto.setAbout("i am java developer");

		User user = serviceImpl.userDtOToUser(userDto);
		List<User> users = new ArrayList<>();
		users.add(user);
		int pageNumber = 0;
		int pageSize = 1;

		Pageable r = PageRequest.of(pageNumber, pageSize);

		when(userRepo.findAll(r)).thenReturn(new PageImpl<>(users));

		List<User> result = serviceImpl.getAllUser(0, 1);

		assertEquals(users, result);

		logger.info("----------------------------------------------------------------------------------------");
	}

	@Test
	public void updateUserTest() {
		long userid = 1L;
		UserDto userDto = new UserDto();
		userDto.setId(1);
		userDto.setName("vaibhav limkar");
		userDto.setEmail("vaibhav123@gmail.com");
		userDto.setPassword("12345");
		userDto.setAbout("i am java developer");

		User user = serviceImpl.userDtOToUser(userDto);

		UserDto userDto1 = new UserDto();
		userDto1.setId(userid);
		userDto1.setName("Soham Sakhare");
		userDto1.setEmail("soham@gmail.com");

		when(userRepo.findById(userid)).thenReturn(java.util.Optional.of(user));

		if (userDto1.getName() != null) {
			user.setName(userDto1.getName());
		}
		if (userDto1.getEmail() != null) {
			user.setEmail(userDto1.getEmail());
		}

		when(userRepo.saveAndFlush(user)).thenReturn(user);

		UserDto expectedResult = serviceImpl.userToDto(user);

		UserDto result = serviceImpl.Update(userDto1, userid);
		assertEquals(expectedResult, result);
		logger.info("----------------------------------------------------------------------------------------");

	}

	@Test
	public void deleteUserTest() {
		long userid = 1L;
		String expectedResult = "";
		User user = new User();
		user.setId(userid);
		user.setName("vaibhav Limakr");
		user.setEmail("vaoibhav@gmial.com");
		user.setPassword("12345");
		user.setAbout("I am Spring boot Developer");
		when(userRepo.findById(userid)).thenReturn(java.util.Optional.of(user));
		if (user != null) {
			logger.info("User is present after performing find function");
		}

		serviceImpl.Delete(userid);

		verify(userRepo, times(1)).delete(user);

		logger.info("----------------------------------------------------------------------------------------");
	}

}
