package com.carpetadigital.ecommerce.Auth;

import com.carpetadigital.ecommerce.entity.Rol;
import com.carpetadigital.ecommerce.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    private RolRepository rolRepository;

   /* public void asignarRol(User user, String rolNombre) {
        Rol rol = rolRepository.findByName(rolNombre)
                .orElseThrow(() -> new RuntimeException("Rol no encontradoooooo"));
        user.getRoles().add(rol);
        // Guardar usuario con nuevo rol
    }*/


    public Rol findByName(String roleUser) {
        return rolRepository.findByName(roleUser)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
    }
}