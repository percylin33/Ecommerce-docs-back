package com.carpetadigital.ecommerce.driver.controller;

import com.carpetadigital.ecommerce.driver.service.GoogleDriveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/api/v1/drive")
public class DriverController {
    @Autowired
    GoogleDriveService googleDriveService;


    @GetMapping()
    public  String sample() throws IOException, GeneralSecurityException {
        return googleDriveService.getfiles();
    }

    @PostMapping()
    @CrossOrigin(origins = "*")
    public  String upload(@RequestParam("file") MultipartFile file) throws IOException, GeneralSecurityException{
        return googleDriveService.uploadFile(file);
    }
}
