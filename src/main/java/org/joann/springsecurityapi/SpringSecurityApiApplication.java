package org.joann.springsecurityapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "org.joann.springsecurityapi.models")
public class SpringSecurityApiApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpringSecurityApiApplication.class, args);

    }

}
