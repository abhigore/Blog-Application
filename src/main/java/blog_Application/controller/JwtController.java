package blog_Application.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import blog_Application.Model.JwtAuthRequest;
import blog_Application.Model.JwtAuthResponse;
import blog_Application.Security.JWTAuth.JwtHelper;
import blog_Application.Security.UserDetails.CustomUserDetailService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;

@RestController
@SecurityRequirement(name="scheme1")
@Tag(name =" JWT Controller" ,description = "Create jwt token here")
public class JwtController {

	@Autowired
	private JwtHelper helper;

	@Autowired
	private CustomUserDetailService customUserDetailService;

	@Autowired
	private AuthenticationManager authenticationManager;

	Logger logger = LoggerFactory.getLogger(JwtController.class);

	@PostMapping("/jwt/token")
	public ResponseEntity<JwtAuthResponse> jwtCreation(@RequestBody JwtAuthRequest authRequest) {
		this.authentication(authRequest.getUsername(), authRequest.getPassword());
		String token = null;

		UserDetails user = customUserDetailService.loadUserByUsername(authRequest.getUsername());
		
		logger.info("enter into the Jwt tokwn creation class");

		try {
			token = helper.generateToken(user);
		} catch (Exception e) {
			logger.info("Provided User is invalid !!");

		}
		JwtAuthResponse response = new JwtAuthResponse();
		response.setToken(token);
		return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.CREATED);
	}

	private void authentication(String username, String password) {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);

		try {
			Authentication authenticate = this.authenticationManager.authenticate(authenticationToken);
		} catch (DisabledException e) {
			throw new DisabledException("Your acoount currently disable. Please Re-Login");
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Invalid Username and Password");
		}

	}
}
