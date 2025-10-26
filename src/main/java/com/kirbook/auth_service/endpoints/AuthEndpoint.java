package com.kirbook.auth_service.endpoints;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.kirbook.auth.GetUserByIdRequest;
import com.kirbook.auth.GetUserByIdResponse;
import com.kirbook.auth.LoginRequest;
import com.kirbook.auth.LoginResponse;
import com.kirbook.auth.SignupRequest;
import com.kirbook.auth.SignupResponse;
import com.kirbook.auth.ValidateTokenRequest;
import com.kirbook.auth.ValidateTokenResponse;
import com.kirbook.auth_service.models.Usuario;
import com.kirbook.auth_service.services.UsuarioService;

import lombok.RequiredArgsConstructor;

@Endpoint
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthEndpoint {

    private static final String NAMESPACE_URI = "http://kirbook.com/auth"; // usa el mismo namespace del XSD
    private final UsuarioService usuarioService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SignupRequest")
    @ResponsePayload
    public SignupResponse signup(@RequestPayload SignupRequest request) {
        SignupResponse response = new SignupResponse();
        boolean registrado = usuarioService.registrarUsuario(request);
        response.setSuccess(registrado);
        response.setMessage(registrado ? "Usuario registrado exitosamente." : "Error al registrar usuario.");
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "LoginRequest")
    @ResponsePayload
    public LoginResponse login(@RequestPayload LoginRequest request) {
        LoginResponse response = new LoginResponse();

        String token = usuarioService.loginUsuario(request.getUsername(), request.getPassword());
        if (token != null) {
            response.setSuccess(true);
            response.setMessage("Login exitoso");
            response.setToken(token);
        } else {
            response.setSuccess(false);
            response.setMessage("Credenciales inválidas");
        }

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ValidateTokenRequest")
    @ResponsePayload
    public ValidateTokenResponse validateToken(@RequestPayload ValidateTokenRequest request) {
        ValidateTokenResponse response = new ValidateTokenResponse();

        var usuarioOpt = usuarioService.validarToken(request.getToken());

        if (usuarioOpt.isPresent()) {
            var usuario = usuarioOpt.get();
            response.setValid(true);
            response.setMessage("Token válido");
            response.setId(usuario.getId());
            response.setUsername(usuario.getUsername());
            response.setEmail(usuario.getEmail());
            response.setNombre(usuario.getNombre());
            response.setApellido(usuario.getApellido());
            response.setRol(usuario.getRol().getNombre().name());
        } else {
            response.setValid(false);
            response.setMessage("Token inválido o expirado");
        }

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetUserByIdRequest")
    @ResponsePayload
    public GetUserByIdResponse obtenerUsuarioPorId(@RequestPayload GetUserByIdRequest request) {
        GetUserByIdResponse response = new GetUserByIdResponse();

        Usuario usuario = usuarioService.buscarPorId(request.getId());
        if (usuario != null) {
            response.setExists(true);
            response.setId(usuario.getId());
            response.setUsername(usuario.getUsername());
            response.setEmail(usuario.getEmail());
            response.setRol(usuario.getRol().getNombre().name());
        } else {
            response.setExists(false);
        }

        return response;
    }
}