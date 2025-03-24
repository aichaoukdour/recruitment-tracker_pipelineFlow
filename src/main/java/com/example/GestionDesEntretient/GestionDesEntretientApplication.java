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
@SpringBootApplication(scanBasePackages = "com.example.GestionDesEntretient")
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
		String encodedAdminPassword = passwordEncoder.encode("admin");
		String encodedUserPassword = passwordEncoder.encode("user");
		String encodedManagerPassword = passwordEncoder.encode("manager");

		UserDetails admin = User.builder()
				.username("admin")
				.password(encodedAdminPassword)
				.build();

		UserDetails user = User.builder()
				.username("user")
				.password(encodedUserPassword)
				.build();

		UserDetails manager = User.builder()
				.username("manager")
				.password(encodedManagerPassword)
				.build();

		return new InMemoryUserDetailsManager(admin, user, manager);
	}
}
