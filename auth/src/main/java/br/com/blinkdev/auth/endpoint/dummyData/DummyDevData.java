package br.com.blinkdev.auth.endpoint.dummyData;

import br.com.blinkdev.auth.endpoint.services.ApplicationUserService;
import br.com.blinkdev.core.model.ApplicationUser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("dev")
@Configuration
class DummyApplicationUse {

    @Bean
    CommandLineRunner initTableUsuario(ApplicationUserService applicationUserService) {
        return args -> {
            applicationUserService.save(new ApplicationUser(1L, "admin", "admin", "ADMIN"));
            applicationUserService.save(new ApplicationUser(2L, "user", "user", "USER"));
        };
    }

}
