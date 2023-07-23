package com.zbro.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.zbro.main.service.UserService;
import com.zbro.main.service.login.ConsumerUserLoginService;
import com.zbro.main.service.login.SellerUserLoginService;
import com.zbro.model.SellerUser;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Autowired
	private UserService userService;
	
	@Order(2)
	@Configuration
	@RequiredArgsConstructor
	public class ConsumerSecurityConfig extends WebSecurityConfigurerAdapter {
		
		private final PasswordEncoder passwordEncoder;
		private final ConsumerUserLoginService consumerUserLoginService;
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
	        http.cors(cors -> cors.disable())
	    		.csrf(csrf -> csrf.disable())
	    		.antMatcher("/**")
	    		.authorizeHttpRequests( requestMatchers -> 
	    			requestMatchers
	    				.antMatchers("/join/**").permitAll()
	    				.antMatchers("/consumer/login").permitAll()
				)
	            .formLogin(login -> login.loginPage("/consumer/login").loginProcessingUrl("/consumer/login").defaultSuccessUrl("/"))
	            .logout(logout -> logout.logoutSuccessUrl("/").invalidateHttpSession(true).deleteCookies("JSESSIONID"));
		}

		@Autowired
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	        auth.userDetailsService(consumerUserLoginService).passwordEncoder(passwordEncoder);
	    }
	}
	
	@Order(1)
	@Configuration
	@RequiredArgsConstructor
	public class SellerSecurityConfig extends WebSecurityConfigurerAdapter {
		
		private final PasswordEncoder passwordEncoder;
	    private final SellerUserLoginService sellerUserLoginService;
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
	        http.cors(cors -> cors.disable())
	    		.csrf(csrf -> csrf.disable())
	    		.antMatcher("/seller/**")
	    		.authorizeHttpRequests( requestMatchers -> 
		    		requestMatchers
						.antMatchers("/seller/login").permitAll()
						.antMatchers("/seller/**").hasAuthority("ROLE_SELLER")
				)
	    		.formLogin(login -> login.loginPage("/seller/login").loginProcessingUrl("/seller/login").successHandler(new SellerLoginSuccessHandler()));
		}

		@Autowired
		protected void configure(AuthenticationManagerBuilder auth) throws Exception { 
	        auth.userDetailsService(sellerUserLoginService).passwordEncoder(passwordEncoder);
	    }
	
		
	}
	
	//판매자 로그인 성공시......
	private class SellerLoginSuccessHandler implements AuthenticationSuccessHandler {
		@Override
		public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
			
			SellerUser seller = userService.getSellerUserByEmail(authentication.getName());
			
			HttpSession session = request.getSession();
			
			session.setAttribute("sellerId", seller.getSellerId());
			session.setAttribute("sellerName", seller.getName());
			session.setAttribute("sellerIsAdmission", seller.isAdmission());
			
			response.sendRedirect("/seller");
		}
	}

}
