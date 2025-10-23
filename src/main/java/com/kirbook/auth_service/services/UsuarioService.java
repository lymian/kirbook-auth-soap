package com.kirbook.auth_service.services;

import com.kirbook.auth.SignupRequest;
import com.kirbook.auth_service.models.Rol;
import com.kirbook.auth_service.models.RolEnum;
import com.kirbook.auth_service.models.Usuario;
import com.kirbook.auth_service.repositories.UsuarioRepository;
import com.kirbook.auth_service.security.JwtUtils;

import io.jsonwebtoken.Claims;

import com.kirbook.auth_service.repositories.RolRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public boolean registrarUsuario(SignupRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            return false;
        }

        Rol rolUser = rolRepository.findByNombre(RolEnum.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setEmail(request.getEmail());
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setTelefono(request.getTelefono());
        usuario.setRol(rolUser);

        usuarioRepository.save(usuario);
        return true;
    }

    public String loginUsuario(String username, String password) {
        return usuarioRepository.findByUsername(username)
                .filter(usuario -> passwordEncoder.matches(password, usuario.getPassword()))
                .map(usuario -> jwtUtils.generateToken(usuario.getUsername(), usuario.getRol().getNombre().name()))
                .orElse(null);
    }
    
    public Optional<Usuario> validarToken(String token) {
        Claims claims = jwtUtils.validateToken(token);
        if (claims == null) return Optional.empty();

        String username = claims.getSubject();
        return usuarioRepository.findByUsername(username);
    }
    
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }
}