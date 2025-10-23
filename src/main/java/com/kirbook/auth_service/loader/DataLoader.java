package com.kirbook.auth_service.loader;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kirbook.auth_service.models.Rol;
import com.kirbook.auth_service.models.RolEnum;
import com.kirbook.auth_service.repositories.RolRepository;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadData(
            RolRepository roleRepository) {
        return args -> {
            if (roleRepository.count() == 0) {
                System.out.println("Creating roles...");
                Rol adminRole = new Rol();
                adminRole.setNombre(RolEnum.ROLE_ADMIN);

                Rol userRole = new Rol();
                userRole.setNombre(RolEnum.ROLE_USER);

                roleRepository.saveAll(List.of(adminRole, userRole));

            } else {
                System.out.println("Roles already exist, skipping creation.");
            }

        };
    }
}