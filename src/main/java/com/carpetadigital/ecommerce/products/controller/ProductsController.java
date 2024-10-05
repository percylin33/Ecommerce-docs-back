package com.carpetadigital.ecommerce.products.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductsController {

    @PostMapping(value = "/demo")
    public String welcome(){
        return "welcome from secure endpoint";
    }


}
