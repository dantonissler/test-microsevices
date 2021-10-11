package br.com.blinkdev.core.docs;


import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

public class BaseSwaggeConfig {
    private final String basePackage;

    public BaseSwaggeConfig(String basePackage) {
        this.basePackage = basePackage;
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Blinkdev - Spring Boot Microservices")
                .description("This application is a test.")
                .termsOfServiceUrl("https://www.linkedin.com/in/danton-issler-rodrigues-8ba01a115/")
                .contact(new Contact("Danton Issler Rodrigues", "https://www.linkedin.com/in/danton-issler-rodrigues-8ba01a115/", "danton.issler18@gmail.com"))
                .license("Todos os direitos reservados.")
                .licenseUrl("https://www.linkedin.com/in/danton-issler-rodrigues-8ba01a115/").version("1.0.0")
                .build();
    }
}
