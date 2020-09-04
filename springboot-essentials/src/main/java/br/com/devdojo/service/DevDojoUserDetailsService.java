package br.com.devdojo.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import br.com.devdojo.repository.DevDojoUserRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DevDojoUserDetailsService  implements UserDetailsService{

	
	private final DevDojoUserRepository devDojoUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		return Optional.ofNullable(devDojoUserRepository.findByusername(username))
				.orElseThrow(()-> new UsernameNotFoundException("User not Found"));
	}
	

}
