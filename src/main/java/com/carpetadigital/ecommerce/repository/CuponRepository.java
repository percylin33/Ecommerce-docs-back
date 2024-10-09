package com.carpetadigital.ecommerce.repository;

import com.carpetadigital.ecommerce.entity.Cupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CuponRepository extends JpaRepository<Cupon, Long> {
    Optional<Cupon> findByCode(String code);
}