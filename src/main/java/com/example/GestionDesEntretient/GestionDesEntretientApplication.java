package com.example.GestionDesEntretient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableAsync
@SpringBootApplication
public class GestionDesEntretientApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionDesEntretientApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
		UserDetails admin = User.builder()
				.username("admin")
				.password(passwordEncoder.encode("admin"))
				.roles("ADMIN")
				.build();

		UserDetails user = User.builder()
				.username("user")
				.password(passwordEncoder.encode("user"))
				.roles("USER")
				.build();

		UserDetails manager = User.builder()
				.username("manager")
				.password(passwordEncoder.encode("manager"))
				.roles("MANAGER")
				.build();

		return new InMemoryUserDetailsManager(admin, user, manager);
	}
}
