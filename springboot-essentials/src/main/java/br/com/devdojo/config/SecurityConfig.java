package br.com.devdojo.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.devdojo.service.DevDojoUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity
@Slf4j
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final DevDojoUserDetailsService devdojoUserDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf()
		.disable()
//		.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//		.and()
		.authorizeRequests()
			.antMatchers("/animes/admin/**").hasRole("ADMIN")
			.antMatchers("/animes/**").hasRole("USER")
			.antMatchers("/actuator/**").permitAll()
			.anyRequest().authenticated()
		.and()
			.formLogin()
		.and()
			.httpBasic();
		 
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		log.info ("Testing encoder password {}", passwordEncoder.encode("academy"));
		auth.inMemoryAuthentication()
		.withUser("devdojo2")
		.password(passwordEncoder.encode("academy"))
		.roles("USER")
		.and()
		.withUser("juliocp2")
		.password(passwordEncoder.encode("academy"))
		.roles("USER","ADMIN");
		
		auth.userDetailsService(devdojoUserDetailsService).passwordEncoder(passwordEncoder);
		
	}
	
	
}
