package com.carpetadigital.ecommerce.document.controller;

import com.carpetadigital.ecommerce.GoogleDrive.DriveService;
import com.carpetadigital.ecommerce.GoogleDrive.Res;
import com.carpetadigital.ecommerce.entity.DocumentsEntity;
import com.carpetadigital.ecommerce.entity.dto.DocumentDto;
import com.carpetadigital.ecommerce.Repository.DocumentsRepository;
import com.carpetadigital.ecommerce.utils.handler.ResponseHandler;
import com.google.api.services.drive.model.File;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RestController
public class DocumentsService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DriveService driveService;

    private final DocumentsRepository documentsRepository;

    public DocumentsService(DocumentsRepository documentsRepository) {
        this.documentsRepository = documentsRepository;
    }

    // servicio para guardar un documento en base de datos
    public Object postDocument(DocumentDto documento) throws GeneralSecurityException, IOException {

        MultipartFile file = documento.getFile();
        // compruebo si el archivo viene vacío o no viene
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("error de recepción de archivo");
        }

        String fileType = file.getContentType();

        // compruebo que la extensión del archivo coincide con el valor de format
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

        if (!extension.equalsIgnoreCase(documento.getFormat())) {
            throw new IllegalArgumentException("no corresponde el tipo de archivo con su formato");
        }

        DocumentsEntity datosIngreso = new DocumentsEntity();
        Optional<DocumentsEntity> tituloEncontrado = documentsRepository.findByTitle(documento.getTitle());
        // compruebo que el contenido del archivo es de tipo pdf, word o imágen
        if (tituloEncontrado.isEmpty()) {
            if (fileType.equals("application/pdf") ||
                    fileType.equals("application/msword") ||
                    fileType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ||
                    fileType.equals("image/jpeg") ||
                    fileType.equals("image/png") ||
                    fileType.equals("image/gif")
            ) {

                Res responseDrive = driveService.uploadFileToDrive(file);

                String fileUrl = responseDrive.getUrl();
                String fileId = responseDrive.getIdFile();
                String webViewLink = responseDrive.getWebViewLink();
                String webContentLink = responseDrive.getWebContentLink();

                String title = documento.getTitle();
                String description = documento.getDescription();
                String format = documento.getFormat();
                Float price = documento.getPrice();
                String category = documento.getCategory();

                datosIngreso.setTitle(title);
                datosIngreso.setDescription(description);
                datosIngreso.setFormat(format);
                datosIngreso.setPrice(price);
                datosIngreso.setCategory(category);

                datosIngreso.setFileUrl(fileUrl);
                datosIngreso.setFileId(fileId);
                datosIngreso.setWebViewLink(webViewLink);
                datosIngreso.setWebContentLink(webContentLink);

                DocumentsEntity documentsEntity = documentsRepository.save(datosIngreso);
//                log.info(" datosIngreso" + datosIngreso);
//                log.info("Documento guardado correctamente" + documentsEntity);
                return documentsEntity;

            } else {
                throw new IllegalArgumentException("el tipo de archivo no es permitido");
            }
        } else {
            throw new IllegalArgumentException("documento existente");
        }
    }

    private Res guardarArchivoEnDrive(MultipartFile file) throws GeneralSecurityException, IOException {
        return driveService.uploadFileToDrive(file);
    }

    // servicio para buscar uno o todos los elementos que coinciden con los valores que vienen en key: value
    public List<DocumentsEntity> getBy(String key, String value) {
        // se crea una lista con todas las posibles key, para evitar inyección
        List<String> validKeys = Arrays.asList("title", "format", "category", "id", "fileUrl", "borradoLogico");

        if (validKeys.contains(key)) {
            // si la key está en la lista entonces armo la petición sql personalizada
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<DocumentsEntity> query = cb.createQuery(DocumentsEntity.class);
            Root<DocumentsEntity> root = query.from(DocumentsEntity.class);

            Predicate condition = cb.equal(root.get(key), value);
            query.select(root).where(condition);

            //retorno los resultados en un array []
            return entityManager.createQuery(query).getResultList();
        } else {
            throw new IllegalArgumentException("pedido no válido");
        }
    }

    // servicio para buscar todos los documentos
    public List<DocumentsEntity> getAllDocuments() {
        try {
            // retorno solo los elementos que no están borrados (valor borradoLogico = false)
            return documentsRepository.findByBorradoLogicoFalse();
        } catch (Exception exception) {
            throw new IllegalArgumentException("error en el servidor");
        }
    }

    // servicio para buscar el documento, recuperar el idFile, buscar el file en google drive y devolverlo al frontend
    public File searchFile(Long id) throws GeneralSecurityException, IOException {
        Optional<DocumentsEntity> documentoEncontrado = documentsRepository.findById(id);
        if (documentoEncontrado.isPresent()) {
            System.out.println("documento encontrado");
            return driveService.buscarFileDrive(documentoEncontrado.get().getFileId());
        } else {
            throw new IllegalArgumentException("error de consistencia de datos");
        }
    }

    //servicio para borrar un documento en forma lógica
    public void borradoLogicoDocument(Long id) {
        Optional<DocumentsEntity> documentoEncontrado = documentsRepository.findById(id);
        if (documentoEncontrado.isPresent()) {
            DocumentsEntity documentoAModificar = documentoEncontrado.get();
            documentoAModificar.setBorradoLogico(!documentoAModificar.getBorradoLogico());
            documentsRepository.save(documentoAModificar);
        } else {
            throw new IllegalArgumentException("error de consistencia de datos");
        }
    }

    // servicio para borrar un documento en forma permanente (fisica)
    public void borradoFisicoDocument(Long id) {
        Optional<DocumentsEntity> documentoEncontrado = documentsRepository.findById(id);
        try {
            if (documentoEncontrado.isPresent()) {
                documentsRepository.deleteById(id);
                driveService.deleteFileDeDrive(documentoEncontrado.get().getFileId());

            } else {
                throw new IllegalArgumentException("error de consistencia de datos");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("error de borrado");
        }

    }

    // servicio para actualizar un documento
    public DocumentsEntity actualizacionDocument(Long id, DocumentDto documentDto) throws Exception {
        Optional<DocumentsEntity> datos = documentsRepository.findById(id);

        if (datos.isPresent()) {
            DocumentsEntity document = datos.get();
            MultipartFile fileUpdate = documentDto.getFile();

            // verifico y actualizo solo los campos que se reciban por body
            if (documentDto.getTitle() != null && !documentDto.getTitle().equals(document.getTitle())) {
                document.setTitle(documentDto.getTitle());
            } else if (documentDto.getPrice() != null && !documentDto.getPrice().equals(document.getPrice())) {
                document.setPrice(documentDto.getPrice());
            } else if (documentDto.getFormat() != null && !documentDto.getFormat().equals(document.getFormat())) {
                document.setFormat(documentDto.getFormat());
            } else if (documentDto.getDescription() != null && !documentDto.getDescription().equals(document.getDescription())) {
                document.setDescription(documentDto.getDescription());
            } else if (documentDto.getCategory() != null && !documentDto.getCategory().equals(document.getCategory())) {
                document.setCategory(documentDto.getCategory());
            } else if (fileUpdate != null) {

                String fileTypeUpdate = fileUpdate.getContentType();
                String fileNameUpdate = fileUpdate.getOriginalFilename();
                String extension = fileNameUpdate.substring(fileNameUpdate.lastIndexOf(".") + 1);

                if (fileTypeUpdate.equals("application/pdf") ||
                        fileTypeUpdate.equals("application/msword") ||
                        fileTypeUpdate.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ||
                        fileTypeUpdate.equals("image/jpeg") ||
                        fileTypeUpdate.equals("image/png") ||
                        fileTypeUpdate.equals("image/gif")
                ) {

                    // busco el archivo en GoogleDrive y lo comparo
                    File file = driveService.buscarFileDrive(document.getFileId());

                    if (file.isEmpty()) {
                        Res responseDriveFileUpdate = driveService.uploadFileToDrive(fileUpdate);

                        document.setFormat(extension);
                        document.setFileId(responseDriveFileUpdate.getIdFile());
                        document.setFileUrl(responseDriveFileUpdate.getUrl());
                        document.setWebViewLink(responseDriveFileUpdate.getWebViewLink());
                        document.setWebContentLink(responseDriveFileUpdate.getWebContentLink());
                    } else {
                        driveService.deleteFileDeDrive(document.getFileId());
                        Res responseDriveFileUpdate = driveService.uploadFileToDrive(fileUpdate);

                        document.setFormat(extension);
                        document.setFileId(responseDriveFileUpdate.getIdFile());
                        document.setFileUrl(responseDriveFileUpdate.getUrl());
                        document.setWebViewLink(responseDriveFileUpdate.getWebViewLink());
                        document.setWebContentLink(responseDriveFileUpdate.getWebContentLink());
                    }
                } else {
                    throw new IllegalArgumentException("el tipo de archivo no es permitido");
                }
            } else {
                throw new IllegalArgumentException("los elementos están actualizados");
            }

            // Guardo los cambios si hay modificaciones

            DocumentsEntity updatedDocument = documentsRepository.save(document);
            return updatedDocument;
        }
        throw new IllegalArgumentException("error al encontrar el elemento a actualizar");
    }
}
