package org.springrestejb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.springrestejb","org.wildfly.demo"})
public class SpringMorderTestApplication {
	
	
	public static void main(String[] args) {
		SpringApplication.run(SpringMorderTestApplication.class, args);
	}

}
