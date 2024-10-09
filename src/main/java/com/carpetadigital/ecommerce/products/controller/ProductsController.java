package com.carpetadigital.ecommerce.products.controller;

import com.carpetadigital.ecommerce.Entity.DocumentsEntity;
import com.carpetadigital.ecommerce.Entity.dto.DocumentDto;
import com.carpetadigital.ecommerce.Repository.DocumentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductsController {

    @Autowired
    public DocumentsRepository documentsRepository;

    @PostMapping("/postDocument")
    public void postDocument(@ModelAttribute @Validated DocumentDto documentDto){
        MultipartFile file = documentDto.getFile();

        if(!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            System.out.println( "archivo recibido: " + fileName);
        }
    }

    @GetMapping("/documents")
    public List<DocumentsEntity> getAllDocuments() {
        System.out.println("hola mundo");
        List<DocumentsEntity> allDocuments = documentsRepository.findAll();
        return allDocuments;
    }

}
