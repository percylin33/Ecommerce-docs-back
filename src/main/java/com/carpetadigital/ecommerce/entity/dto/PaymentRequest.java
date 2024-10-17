package com.carpetadigital.ecommerce.entity.dto;





import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    private Long userId;
    private Double amount;
    private boolean isSubscription;
    private String status;
    private String subscriptionType;
    private List<Long> documentIds;  // IDs de los documentos comprados
    private String guestEmail;
    private String subject;
    private String body;
    private Integer template;
    private List<String> attachments;  // Archivos adjuntos
    private List<String> cc;           // Correos en copia
    private List<String> bcc;          // Correos en copia oculta

    // Getters y Setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public boolean isSubscription() {
        return isSubscription;
    }

    public void setSubscription(boolean subscription) {
        isSubscription = subscription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public List<Long> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<Long> documentIds) {
        this.documentIds = documentIds;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }

    public List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public void setBcc(List<String> bcc) {
        this.bcc = bcc;
    }
}
