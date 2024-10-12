package com.carpetadigital.ecommerce.cupon.service;

import com.carpetadigital.ecommerce.entity.Cupon;
import com.carpetadigital.ecommerce.entity.dto.CuponDTO;
import com.carpetadigital.ecommerce.repository.CuponRepository;
import com.carpetadigital.ecommerce.utils.exception.common.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Service
public class CuponService {

    @Autowired
    private CuponRepository cuponRepository;

    public Cupon createCupon(CuponDTO cuponDTO) {
        if (cuponDTO.getDiscountValue() <= 0 || cuponDTO.getDiscountValue() > 100) {
            throw new IllegalArgumentException("El valor del descuento debe ser entre 1 y 100%");
        }

        if (cuponDTO.getStartDate() == null || cuponDTO.getExpirationDate() == null ||
                cuponDTO.getStartDate().isAfter(cuponDTO.getExpirationDate())) {
            throw new IllegalArgumentException("Las fechas de inicio y expiración deben ser válidas");
        }

        // Generar el código de manera automática
        String generatedCode = generateCouponCode();

        Cupon cupon = new Cupon();
        cupon.setCode(generatedCode); // Establece el código generado
        cupon.setDiscountValue(cuponDTO.getDiscountValue());
        cupon.setStartDate(cuponDTO.getStartDate());
        cupon.setExpirationDate(cuponDTO.getExpirationDate());
        cupon.setIsActive(cuponDTO.getIsActive());
        cupon.setCreated_at(LocalDate.now()); // Establece la fecha de creación a la fecha actual

        return cuponRepository.save(cupon);
    }

    // Método para generar un código aleatorio
    public String generateCouponCode() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public boolean validateCupon(String code) throws Exception {
        Cupon cupon = cuponRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Cupón no encontrado"));

        log.info("validateCupon" + cupon.getStartDate());
        // Verificar si el cupón está activo
        if (!cupon.getIsActive()) {
            throw new Exception ("El cupón no está activo");
        }

        // Verificar si la fecha de inicio ya ha pasado o es hoy
        LocalDate today = LocalDate.now();
        if (today.isBefore(cupon.getStartDate())) {
            throw new Exception("El cupón aún no es válido: " + cupon.getStartDate());
        }


        // Verificar si el cupón ha expirado
        if (today.isAfter(cupon.getExpirationDate())) {
            throw new Exception("El cupón ha expirado");
        }

        return true; // El cupón es válido
    }
}
