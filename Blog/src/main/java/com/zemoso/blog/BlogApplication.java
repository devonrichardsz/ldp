package com.zemoso.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableReactiveMongoRepositories
@SpringBootApplication
public class BlogApplication {
    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create();
    }
 
    @Override
    protected String getDatabaseName() {
        return "reactive";
    }

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}
}
