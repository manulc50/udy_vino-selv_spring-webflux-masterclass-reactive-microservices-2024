package com.mlorenzo.reactiveuserservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mlorenzo.reactiveuserservice.dto.UserDto;
import com.mlorenzo.reactiveuserservice.service.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("all")
	public Flux<UserDto> getAll() {
		return userService.getAll();
	}
	
	@GetMapping("{id}")
	public Mono<ResponseEntity<UserDto>> getUserById(@PathVariable(name = "id") int userId) {
		return userService.getUserById(userId)
				.map(uDto -> ResponseEntity.ok().body(uDto))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<UserDto> createUser(@RequestBody Mono<UserDto> userDto) {
		return userService.createUser(userDto);
	}
	
	@PutMapping("{id}")
	public Mono<ResponseEntity<UserDto>> updateUser(@PathVariable(name = "id") int userId, @RequestBody Mono<UserDto> userDto) {
		return userService.updateUser(userId, userDto)
				.map(uDto -> ResponseEntity.ok().body(uDto))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> deleteUser(@PathVariable int id) {
		return userService.deleteUser(id);
	}
}
