package com.kirbook.auth_service.repositories;

import com.kirbook.auth_service.models.Usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByUsername(String username);

    Optional<Usuario> findByUsername(String username);
}