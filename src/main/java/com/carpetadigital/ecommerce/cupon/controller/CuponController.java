package com.carpetadigital.ecommerce.cupon.controller;

import com.carpetadigital.ecommerce.cupon.service.CuponService;
import com.carpetadigital.ecommerce.entity.Cupon;
import com.carpetadigital.ecommerce.entity.dto.CuponDTO;
import com.carpetadigital.ecommerce.utils.handler.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cupons")
public class CuponController {
    @Autowired
    private CuponService cuponService;
    @PostMapping("/create")
    public ResponseEntity<Object> createCupon(@RequestBody CuponDTO cuponDTO) {
        return  ResponseHandler.generateResponse(HttpStatus.OK,
                cuponService.createCupon(cuponDTO),
                true);
    }

    @GetMapping("/validate/{code}")
    public ResponseEntity<Object> validateCupon(@PathVariable String code) throws Exception {
        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                cuponService.validateCupon(code),
                true);

    }
}
