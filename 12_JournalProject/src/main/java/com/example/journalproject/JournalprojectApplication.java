package com.example.journalproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableTransactionManagement
public class JournalprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(JournalprojectApplication.class, args);
	}
	// MONGODB TRANSACTIONAL MANAGER !
	@Bean
	public PlatformTransactionManager xyz(MongoDatabaseFactory dbFactory) {
		return new MongoTransactionManager(dbFactory);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate() ;
	}
//	❓ Why is it necessary to define RestTemplate as a @Bean?
//	Because Spring only injects (auto-wires) things that it knows how to create — and RestTemplate is not auto-created by default.
//	you're telling Spring:
//			“Hey Spring, whenever someone needs a RestTemplate, use this one!”
//			✅ Without @Bean:
//	Spring doesn't know how to create it → You get an error.
//
//			✅ With @Bean:
//	Spring creates one RestTemplate and injects it wherever you need it.




	//	🧩 Summary (in tech terms):
	//	@Transactional tells Spring, "Treat this method as an all-or-nothing operation."
	//	The MongoTransactionManager bean tells Spring how to start, commit, and rollback transactions specifically for MongoDB.
	//	Without this bean, Spring won’t know how to enforce @Transactional with MongoDB.

}
