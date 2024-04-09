package blog_Application.Myconfig;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import blog_Application.Repository.CategoryRepo;
import blog_Application.Repository.UserRepo;
import blog_Application.ServiceImpl.CategoryServiceImpl;
import blog_Application.ServiceImpl.UserServiceImpl;

@Configuration
public class Myconfig {
	
	@Bean
	public ModelMapper modelMapper()
	{
		return new ModelMapper();
	}
	
	

}


