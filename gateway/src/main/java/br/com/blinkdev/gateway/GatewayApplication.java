package br.com.blinkdev.gateway;

import br.com.blinkdev.core.property.JwtConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
@EnableConfigurationProperties(value = JwtConfiguration.class)
@ComponentScan("br.com.blinkdev")
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
