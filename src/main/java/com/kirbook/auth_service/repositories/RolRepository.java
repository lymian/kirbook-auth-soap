package com.kirbook.auth_service.repositories;

import com.kirbook.auth_service.models.Rol;
import com.kirbook.auth_service.models.RolEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(RolEnum nombre);
}