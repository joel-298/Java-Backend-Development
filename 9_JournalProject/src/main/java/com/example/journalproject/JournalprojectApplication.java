package com.example.journalproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

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
	//	🧩 Summary (in tech terms):
	//	@Transactional tells Spring, "Treat this method as an all-or-nothing operation."
	//	The MongoTransactionManager bean tells Spring how to start, commit, and rollback transactions specifically for MongoDB.
	//	Without this bean, Spring won’t know how to enforce @Transactional with MongoDB.


}
