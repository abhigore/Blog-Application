package blog_Application.Security.JWTAuth;

import java.io.IOException;

import javax.naming.MalformedLinkException;

import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import blog_Application.Security.UserDetails.CustomUserDetailService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFiler extends OncePerRequestFilter{
	
	@Autowired
	private JwtHelper helper;
	
	@Autowired
	@Lazy
	private CustomUserDetailService customUserDetailService;
	
	
	Logger logger =LoggerFactory.getLogger(JwtAuthenticationFiler.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException
	{
		
		
		String authenticationHeader =request.getHeader("Authorization");
		String token =null;
		String username =null;
		
		//String header =authenticationHeader.substring(0, 5);
		
		if(authenticationHeader!=null && authenticationHeader.startsWith("Bearer"))	
		{
			 token =authenticationHeader.substring(7);
			
			try {
				 username = helper.getUsernameFromToken(token);
				
				
			}catch (IllegalArgumentException ex) {
		       throw new IllegalIdentifierException("User not found with token" + token);
			}
			catch(ExpiredJwtException ex) {
				System.out.println("Your Requested Token is expired .Please create new Token"); 
				logger.info("Your Requested Token is expired .Please create new Token");
			}
			catch(MalformedJwtException ex)
			{
				System.out.println("Invalid JWT token");
				logger.info("Invalid JWT token");
			}
		}
		else
		{
			System.out.println("Header is empty (or) Entered token does not contain the");
			logger.info("Header is empty (or) Entered token does not contain the");
			
		}
		
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
		{
			UserDetails user = customUserDetailService.loadUserByUsername(username);
			
			if(this.helper.validateToken(token, user))
			{
				System.out.println(user.getUsername() + "   " + " password" +  user.getPassword() + "  authorities" +user.getAuthorities());
			
				
			try {
				
				UsernamePasswordAuthenticationToken authenticationToken =new UsernamePasswordAuthenticationToken(user,null, user.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				
				logger.info("token Successfully verified");
			}
			catch (Exception e) {
				logger.info("Username and Password are invalid");
			}
			
		}
		else
		{
			logger.info("Token is Invalid. please create new JWT token");
		}
		}
		else
		{
			logger.info("Invalid JWT token");
		}
		
		
		filterChain.doFilter(request, response);
		
		logger.info("complete filtercahin process");
	}
	


}
