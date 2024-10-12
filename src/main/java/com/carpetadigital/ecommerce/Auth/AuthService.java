package com.carpetadigital.ecommerce.Auth;

import com.carpetadigital.ecommerce.Jwt.JwtService;
import com.carpetadigital.ecommerce.User.Role;
import com.carpetadigital.ecommerce.User.User;
import com.carpetadigital.ecommerce.User.UserRepository;
import com.carpetadigital.ecommerce.entity.Rol;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService rolService;
    private final AuthenticationManager authenticationManager;
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse register(RegisterRequest request) {
        log.info("Registrando usuario: {}", request);

        // Obtener el rol de la base de datos
        Rol userRole = rolService.findByName(request.getRol());

        if (userRole == null) {
            throw new RuntimeException("Rol no encontrado");
        }

        // Crear un nuevo usuario y asignar el rol
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .country(request.getCountry())
                .email(request.getEmail())
                .rol(userRole) // Asignar el rol al usuario
                .build();
        log.info("user "+user);

        // Guardar el usuario
        User savedUser = userRepository.save(user);

        if (savedUser == null) {
            throw new RuntimeException("Error al guardar el usuario");
        }

        // Devolver la respuesta de autenticación
        return AuthResponse.builder()
                .token(jwtService.getToken(savedUser))
                .build();
    }

    public void logout(String token) {
        // Aquí puedes invalidar el token JWT, por ejemplo, añadiéndolo a una lista de tokens inválidos
        jwtService.invalidateToken(token);
    }
}
