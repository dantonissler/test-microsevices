package br.com.blinkdev.auth;

import br.com.blinkdev.core.property.JwtConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan({"br.com.blinkdev.core.model"})
@EnableJpaRepositories({"br.com.blinkdev.core.repository"})
@EnableEurekaClient
@EnableConfigurationProperties(value = JwtConfiguration.class)
@ComponentScan("br.com.blinkdev")
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}

