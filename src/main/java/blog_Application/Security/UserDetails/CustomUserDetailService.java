package blog_Application.Security.UserDetails;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import blog_Application.Exception.ResourceNotFoundException;
import blog_Application.Model.User;
import blog_Application.Repository.RoleRepo;
import blog_Application.Repository.UserRepo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
public class CustomUserDetailService implements UserDetailsService{

	@Autowired
	@Lazy
	private UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 
		User user = userRepo.findByEmail(username).orElseThrow(()->new ResourceNotFoundException("User", "email",username));
		
		return new CustomUserDetails(user);
	}

}
