package com.dawan.cinema.config;


import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
	
	//Spring doc configuration
	
	@Bean
	public OpenAPI openApi() {
		
		Server devServer = new Server();
		devServer.setUrl("http://localhost:8080");
		devServer.setDescription("Server URL in Dev. environment");
		
		Server prodServer = new Server();
		prodServer.setUrl("http://localhost:8085");
		prodServer.setDescription("Server URL in Prod. environment");
		
		Contact contact = new Contact();
		contact.setEmail("contact@dawan.fr");
		contact.setName("Admin Dawan");
		contact.setUrl("https://www.dawan.fr");
		
		License mitLIcence = new License().name("Dawan Licence").url("https://www.dawan.fr/licence/mit/");
		
		Info info = new Info()
				.title("Demo Spring Web Rest API")
				.version("1.0")
				.contact(contact)
				.description("This Api exposes endpoints to manage tutorials....")
				.termsOfService("https://www.dawan.fr/termsOfService").license(mitLIcence);
		
		return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
		
	}

}