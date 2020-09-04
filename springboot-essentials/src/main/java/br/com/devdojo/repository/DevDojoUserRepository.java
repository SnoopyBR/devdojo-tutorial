package br.com.devdojo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.devdojo.domain.DevDojoUser;

public interface DevDojoUserRepository extends JpaRepository<DevDojoUser, Integer>{
	
	DevDojoUser findByusername(String username);

}
