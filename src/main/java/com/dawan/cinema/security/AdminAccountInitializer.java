package com.dawan.cinema.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dawan.cinema.entities.Role;
import com.dawan.cinema.entities.User;
import com.dawan.cinema.repositories.UserRepository;

/**
 * Crée le premier compte administrateur au démarrage s'il n'en existe aucun,
 * à partir de ADMIN_USERNAME / ADMIN_PASSWORD (jamais de mot de passe dans le code).
 */
@Component
public class AdminAccountInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminAccountInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String username;
    private final String password;

    public AdminAccountInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder,
            @Value("${app.admin.username:}") String username,
            @Value("${app.admin.password:}") String password) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.username = username;
        this.password = password;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.existsByRole(Role.ADMIN)) {
            return;
        }
        if (username.isBlank() || password.isBlank()) {
            log.warn("Aucun administrateur : définissez ADMIN_USERNAME et ADMIN_PASSWORD dans le fichier .env.");
            return;
        }
        User admin = new User();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setRole(Role.ADMIN);
        admin.setFirstName("Admin");
        admin.setLastName("CC8");
        userRepository.save(admin);
        log.info("Compte administrateur « {} » créé.", username);
    }
}