package blog_Application.Myconfig;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class Myconfig {
	
	@Bean
	public ModelMapper modelMapper()
	{
		return new ModelMapper();
	}
	

}
