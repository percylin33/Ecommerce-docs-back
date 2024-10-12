package com.carpetadigital.ecommerce.document.controller;

import com.carpetadigital.ecommerce.entity.DocumentsEntity;
import com.carpetadigital.ecommerce.entity.dto.DocumentDto;
import com.carpetadigital.ecommerce.Repository.DocumentsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RestController
public class DocumentsService {

    @Autowired
    private EntityManager entityManager;

    private final DocumentsRepository documentsRepository;

    public DocumentsService(DocumentsRepository documentsRepository) {
        this.documentsRepository = documentsRepository;
    }

    // servicio para guardar un documento en base de datos
    public Object postDocument(DocumentDto documento) {
        MultipartFile file = documento.getFile();
        // compruebo si el archivo viene vacío o no viene
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("error de recepción de archivo");
        }

        String fileType = file.getContentType();

        // compruebo que la extensión del archivo coincide con el valor de format
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        System.out.println(extension);
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
                String fileUrl = "http://prueba";
                String fileId = "kajdsha2dasf2a2d2fa2df2a";
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

                documentsRepository.save(datosIngreso);
                return datosIngreso;

            } else {
                throw new IllegalArgumentException("el tipo de archivo no es permitido");
            }
        } else {
            throw new IllegalArgumentException("documento existente");
        }
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
}
