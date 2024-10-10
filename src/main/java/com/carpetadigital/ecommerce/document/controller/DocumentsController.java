package com.carpetadigital.ecommerce.document.controller;

import com.carpetadigital.ecommerce.Entity.DocumentsEntity;
import com.carpetadigital.ecommerce.Entity.dto.DocumentDto;
import com.carpetadigital.ecommerce.Repository.DocumentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class DocumentsController {

    @Autowired
    public DocumentsService documentsService;

    private final DocumentsRepository documentsRepository;

    public DocumentsController(DocumentsRepository documentsRepository) {
        this.documentsRepository = documentsRepository;
    }

    // guardado de un documento
    @PostMapping("/postDocument")
    public Object postDocument(@ModelAttribute @Validated DocumentDto documentDto) {
        MultipartFile file = documentDto.getFile();
        String fileType = file.getContentType();
        DocumentsEntity datosIngreso = new DocumentsEntity();

        List<DocumentsEntity> tituloEncontrado = documentsService.getBy("title", documentDto.getTitle());

        if (tituloEncontrado.isEmpty()) {
            if (!file.isEmpty()) {
                if (fileType.equals("application/pdf") ||
                        fileType.equals("application/msword") ||
                        fileType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {

                    String fileUrl = "http://prueba";
                    String title = documentDto.getTitle();
                    String description = documentDto.getDescription();
                    String format = documentDto.getFormat();
                    Float price = documentDto.getPrice();
                    String category = documentDto.getCategory();


                    datosIngreso.setTitle(title);
                    datosIngreso.setDescription(description);
                    datosIngreso.setFormat(format);
                    datosIngreso.setPrice(price);
                    datosIngreso.setCategory(category);
                    datosIngreso.setFileUrl(fileUrl);

                    documentsRepository.save(datosIngreso);

                    String fileName = file.getOriginalFilename();
                    System.out.println("archivo recibido: ");

                }
            }
            return datosIngreso;
        }
        return tituloEncontrado;
    }

    // buscar todos los documentos
    @GetMapping("/documents")
    public List<DocumentsEntity> getAllDocuments() {
        System.out.println("hola mundo");
        List<DocumentsEntity> allDocuments = documentsRepository.findAll();
        return allDocuments;
    }

    @GetMapping("/searchDocumentBy")
    public List<DocumentsEntity> searchDocumentBy (
            @RequestParam String key,
            @RequestParam String value) {
        List<DocumentsEntity> tituloEncontrado = documentsService.getBy(key, value);
        return tituloEncontrado;

    }

}
