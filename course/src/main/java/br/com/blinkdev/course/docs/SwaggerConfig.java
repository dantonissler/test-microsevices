package br.com.blinkdev.course.docs;

import br.com.blinkdev.core.docs.BaseSwaggeConfig;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggeConfig {
    public SwaggerConfig() {
        super("br.com.blinkdev.course.endpoint.controller");
    }
}
