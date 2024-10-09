package com.carpetadigital.ecommerce.products.controller;

import com.carpetadigital.ecommerce.Entity.DocumentsEntity;
import com.carpetadigital.ecommerce.Repository.DocumentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductsController {

    @Autowired
    public DocumentsRepository documentsRepository;

    @PostMapping(value = "/demo")
    public String welcome(){
        return "welcome from secure endpoint";
    }

    @GetMapping("/documents")
    public List<DocumentsEntity> getAllDocuments() {
        System.out.println("hola mundo");
        List<DocumentsEntity> allDocuments = documentsRepository.findAll();
        return allDocuments;
    }

}
