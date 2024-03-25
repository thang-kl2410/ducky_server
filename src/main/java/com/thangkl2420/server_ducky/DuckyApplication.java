package com.thangkl2420.server_ducky;

import com.thangkl2420.server_ducky.dto.RegisterRequest;
import com.thangkl2420.server_ducky.service.AuthenticationService;
import com.thangkl2420.server_ducky.service.ConversationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static com.thangkl2420.server_ducky.entity.Role.ADMIN;
import static com.thangkl2420.server_ducky.entity.Role.MANAGER;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class DuckyApplication {
	public static void main(String[] args) {
		SpringApplication.run(DuckyApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service,
			ConversationService conversationService
	) {
		return args -> {
//			var admin = RegisterRequest.builder()
//					.firstname("Admin")
//					.lastname("Admin")
//					.email("admin@gmail.com")
//					.password("123456")
//					.role(ADMIN)
//					.build();
//			System.out.println("Admin token: " + service.register(admin).getAccessToken());
//
//			var manager = RegisterRequest.builder()
//					.firstname("Manager")
//					.lastname("Manager")
//					.email("manager@gmail.com")
//					.password("123456")
//					.role(MANAGER)
//					.build();
//			System.out.println("Manager token: " + service.register(manager).getAccessToken());
		};
	}
}
