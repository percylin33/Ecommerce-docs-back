package com.carpetadigital.ecommerce.repository;

import com.carpetadigital.ecommerce.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByName(String nombre);
}
