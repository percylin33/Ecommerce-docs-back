package com.carpetadigital.ecommerce.document.controller;

import com.carpetadigital.ecommerce.entity.DocumentsEntity;
import com.carpetadigital.ecommerce.entity.dto.DocumentDto;
import com.carpetadigital.ecommerce.Repository.DocumentsRepository;
import com.carpetadigital.ecommerce.utils.handler.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/document")
public class DocumentsController {

    @Autowired
    public DocumentsService documentsService;

    private final DocumentsRepository documentsRepository;

    public DocumentsController(DocumentsRepository documentsRepository) {
        this.documentsRepository = documentsRepository;
    }

    // guardado de un documento
    @PostMapping()
    public Object postDocument(@ModelAttribute @Validated DocumentDto documentDto) {
        Object documentoGuardado = documentsService.postDocument(documentDto);
        return ResponseHandler.generateResponse(
                HttpStatus.CREATED,
                documentoGuardado,
                true
        );
    }

    // buscar todos los documentos
    @GetMapping()
    public ResponseEntity<Object> getAllDocuments() {
        List<DocumentsEntity> allDocuments = documentsService.getAllDocuments();
        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                allDocuments,
                true
        );
    }

    // buscar un elemento por key: value (búsqueda dinámica)
    @GetMapping("/searchBy")
    public ResponseEntity<Object> searchDocumentBy(@RequestParam String key, @RequestParam String value) {
        List<DocumentsEntity> documentoEncontrado = documentsService.getBy(key, value);
        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                documentoEncontrado,
                true
        );
    }

    // borrado lógico del documento
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> borradoLogicoDocument(@PathVariable Long id) {
        documentsService.borradoLogicoDocument(id);
        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                null,
                true
        );
    }
}
