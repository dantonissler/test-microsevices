package br.com.blinkdev.core.repository;


import br.com.blinkdev.core.model.ApplicationUser;
import br.com.blinkdev.core.model.Course;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationUserRepository extends PagingAndSortingRepository<ApplicationUser, Long> {
    Optional<ApplicationUser> findByUsername(String username);
}
