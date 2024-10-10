package com.carpetadigital.ecommerce.document.controller;

import com.carpetadigital.ecommerce.Entity.DocumentsEntity;
import com.carpetadigital.ecommerce.Repository.DocumentsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@Service
@RestController
public class DocumentsService {

    @Autowired
    private EntityManager entityManager;

    private final DocumentsRepository documentsRepository;

    public DocumentsService(DocumentsRepository documentsRepository) {
        this.documentsRepository = documentsRepository;
    }

    public List<DocumentsEntity> getBy(String key, String value) {
        // se crea una lista con todas las posibles key, para evitar inyección
        List<String> validKeys = Arrays.asList("title", "description", "format", "category", "id", "price", "fileUrl", "borradoLogico");

        if (validKeys.contains(key)) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<DocumentsEntity> query = cb.createQuery(DocumentsEntity.class);
            Root<DocumentsEntity> root = query.from(DocumentsEntity.class);

            Predicate condition = cb.equal(root.get(key), value);
            query.select(root).where(condition);

            return entityManager.createQuery(query).getResultList();

        }
        return null;
    }
}
