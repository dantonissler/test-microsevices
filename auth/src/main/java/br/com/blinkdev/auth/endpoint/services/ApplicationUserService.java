package br.com.blinkdev.auth.endpoint.services;

import br.com.blinkdev.core.model.ApplicationUser;
import org.springframework.stereotype.Service;

@Service
public interface ApplicationUserService {
    public ApplicationUser save(ApplicationUser applicationUser);
}
