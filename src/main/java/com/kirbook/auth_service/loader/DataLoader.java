package com.kirbook.auth_service.loader;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.kirbook.auth_service.models.Rol;
import com.kirbook.auth_service.models.RolEnum;
import com.kirbook.auth_service.models.Usuario;
import com.kirbook.auth_service.repositories.RolRepository;
import com.kirbook.auth_service.repositories.UsuarioRepository;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadData(
            RolRepository roleRepository, UsuarioRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (roleRepository.count() == 0) {
                System.out.println("Creating roles...");
                Rol adminRole = new Rol();
                adminRole.setNombre(RolEnum.ROLE_ADMIN);

                Rol userRole = new Rol();
                userRole.setNombre(RolEnum.ROLE_USER);

                roleRepository.saveAll(List.of(adminRole, userRole));

                // Crear usuarios
                System.out.println("Creating users...");
                Usuario adminUser = new Usuario();
                adminUser.setUsername("admin");
                adminUser.setPassword(passwordEncoder.encode("123456"));
                adminUser.setRol(adminRole);
                adminUser.setEmail("admin@kirbook.com");
                adminUser.setTelefono("1234567890");
                adminUser.setNombre("Admin");
                adminUser.setApellido("User");
                userRepository.save(adminUser);

                // crear usuario normal
                Usuario normalUser = new Usuario();
                normalUser.setUsername("juanperez");
                normalUser.setPassword(passwordEncoder.encode("123456"));
                normalUser.setRol(userRole);
                normalUser.setEmail("juanperez@kirbook.com");
                normalUser.setTelefono("0987654321");
                normalUser.setNombre("Juan");
                normalUser.setApellido("Pérez");
                userRepository.save(normalUser);

                System.out.println("Data loading completed.");

            } else {
                System.out.println("Roles already exist, skipping creation.");
            }

        };
    }
}