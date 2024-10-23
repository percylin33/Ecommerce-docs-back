package com.carpetadigital.ecommerce.document.controller;

import com.carpetadigital.ecommerce.entity.dto.DocumentDto;
import com.carpetadigital.ecommerce.Repository.DocumentsRepository;
import com.carpetadigital.ecommerce.utils.handler.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;


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
    public Object postDocument(@ModelAttribute @Validated DocumentDto documentDto) throws GeneralSecurityException, IOException {
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
        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                documentsService.getAllDocuments(),
                true
        );
    }

    // buscar un elemento por key: value (búsqueda dinámica)
    @GetMapping("/searchBy")
    public ResponseEntity<Object> searchDocumentBy(@RequestParam String key, @RequestParam String value) {
        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                documentsService.getBy(key, value),
                true
        );
    }

    // buscar archivo de un documento específico por Id
    @GetMapping("/file/{id}")
    public ResponseEntity<Object> searchFile(@PathVariable Long id) throws GeneralSecurityException, IOException {
        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                documentsService.searchFile(id),
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

    // elimina documento físicamente (base de datos y google drive)
    @DeleteMapping("/fisico/{id}")
    public ResponseEntity<Object> borradoFisicoDocument(@PathVariable Long id) {
        documentsService.borradoFisicoDocument(id);
        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                null,
                true
        );
    }

    // actualización de un documento
    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizacionDocument(@PathVariable Long id, @ModelAttribute DocumentDto documentDto) {
        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                documentsService.actualizacionDocument(id, documentDto),
                true
        );
    }
}
