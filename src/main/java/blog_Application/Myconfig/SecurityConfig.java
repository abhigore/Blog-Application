package blog_Application.Myconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import blog_Application.Security.JWTAuth.JWTAuthenticationEntryPoint;
import blog_Application.Security.JWTAuth.JwtAuthenticationFiler;
import blog_Application.Security.UserDetails.CustomUserDetailService;



@Configuration
@EnableMethodSecurity
@EnableWebMvc
public class SecurityConfig {

    @Autowired
    private UserDetailsService detailsService;
    
    @Autowired
    private JwtAuthenticationFiler jwtAuthenticationFiler;
    
    @Autowired
    private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    
    
    
	@Bean
	public PasswordEncoder encoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filter(HttpSecurity http) throws Exception
	{
		http.
		csrf((csrf)->csrf.disable())
		.authorizeHttpRequests((auth)->auth
				.requestMatchers("/v3/api-docs",
						"/swagger-ui/**",
						"/swagger/resources/**",
						"/webjars/**",
						"/blog-application-documentation"
						).permitAll().requestMatchers(HttpMethod.GET).permitAll())
		.authorizeHttpRequests((auth)->auth.requestMatchers("/user/*/role",
				"/jwt/token",
				"/user",
				"/user/*/post/*/comment",
				"/category",
				"/user/*/category/*/post",
				"/error").permitAll()
		.requestMatchers(
				"/category/**",
				"/comments", "comment/**","/comments",
				"/posts", "/post/**", "/user/*/posts", "/category/*/posts",
				"/users" ,"user/**",
				"/role/**", "/roles"
				).hasRole("ADMIN").anyRequest().authenticated())
		.exceptionHandling((exc)->exc.authenticationEntryPoint(jwtAuthenticationEntryPoint))
		.sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		;
		http.addFilterAfter(jwtAuthenticationFiler, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
	
	@Bean
	public UserDetailsService userDetails() throws Exception
	{
		return new CustomUserDetailService();
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthProvider() throws Exception
	{
	   DaoAuthenticationProvider daoProvider =new DaoAuthenticationProvider();
	   daoProvider.setUserDetailsService(detailsService);
	   daoProvider.setPasswordEncoder(encoder());
	   
	   return daoProvider;
	}
	
     @Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
	{
		return config.getAuthenticationManager();
				
	}
}


