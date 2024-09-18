package com.mlorenzo.reactiveuserservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mlorenzo.reactiveuserservice.dto.UserDto;
import com.mlorenzo.reactiveuserservice.repository.UserRepository;
import com.mlorenzo.reactiveuserservice.util.EntityDtoUtil;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public Flux<UserDto> getAll() {
		return userRepository.findAll()
				.map(EntityDtoUtil::toDto); // Versión simplificada de la expresión "user -> EntityDtoUtil.toDto(user)"
	}
	
	public Mono<UserDto> getUserById(int id) {
		return userRepository.findById(id)
				.map(EntityDtoUtil::toDto); // Versión simplificada de la expresión "user -> EntityDtoUtil.toDto(user)"
	}
	
	public Mono<UserDto> createUser(Mono<UserDto> userDto) {
		return userDto
				.map(EntityDtoUtil::toEntity) // Versión simplificada de la expresión "uDto -> EntityDtoUtil.toEntity(uDto)"
				.flatMap(userRepository::save) // Versión simplificada de la expresión "user -> userRepository.save(user)"
				.map(EntityDtoUtil::toDto); // Versión simplificada de la expresión "user -> EntityDtoUtil.toDto(user)"
	}
	
	public Mono<UserDto> updateUser(int id, Mono<UserDto> userDto) {
		return userRepository.findById(id)
				.flatMap(user -> userDto
						.map(EntityDtoUtil::toEntity) // Versión simplificada de la expresión "uDto -> EntityDtoUtil.toEntity(uDto)"
						.doOnNext(u -> u.setId(id)))
				.flatMap(userRepository::save) // Versión simplificada de la expresión "user -> userRepository.save(user)"
				.map(EntityDtoUtil::toDto); // Versión simplificada de la expresión "user -> EntityDtoUtil.toDto(user)"
	}
	
	public Mono<Void> deleteUser(int id) {
		return userRepository.deleteById(id);
	}
}
