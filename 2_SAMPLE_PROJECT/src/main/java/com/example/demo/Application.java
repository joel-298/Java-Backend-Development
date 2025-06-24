package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // Only assigned on main class !
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

// SPRING BOOT APPLICATION WORKING : 
// 1) @Configuration : 
// If use @Configuration on a class then that means that Class is expected to return some CONFIGURATIONS ! 
// Usually configuration annotation is used with another annotation which @Bean 
// Now we know Bean annotation is made with @Component, and @RestController but RESTCONTROLLER conatins some extra features !  
// NOW BEAN ANNOTATION CAN ALSO BE MADE USING @Bean : BUT @BEAN is used on 'FUNCTIONS' and not on 'CLASS' ;
 

// 2) @EnableAutoConfiguration : 
//								 PROBLEM STATEMENT : For example if we want to connect MongoDB then usually 1st we have to write all the configurtions and then write some code for connection right ? 
//                               BUT : in here what we can do is mention all the configurations and then write @ ENABLE AUTO CONFIGURATION : So its automatically going to connect with the DB

// 3) @ComponentScan : - Scan the '@ Beans' in the whole application and add them in IOC Container !
// 					   - Will scan in demo package only ! 