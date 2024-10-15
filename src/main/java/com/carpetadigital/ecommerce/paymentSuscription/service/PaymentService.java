package com.carpetadigital.ecommerce.paymentSuscription.service;

import com.carpetadigital.ecommerce.entity.Payment;
import com.carpetadigital.ecommerce.entity.Subscription;
import com.carpetadigital.ecommerce.entity.dto.PaymentSuscriptionDto;
import com.carpetadigital.ecommerce.repository.PaymentRepository;
import com.carpetadigital.ecommerce.repository.SubscriptionRepository;
import jakarta.mail.MessagingException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.carpetadigital.ecommerce.entity.DocumentsEntity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final JavaMailSender mailSender;
    private final SubscriptionRepository subscriptionRepository;
    private final EmailService emailService;
    private final com.carpetadigital.ecommerce.Repository.DocumentsRepository documentsRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, JavaMailSender mailSender,
                          SubscriptionRepository subscriptionRepository, EmailService emailService,
                          com.carpetadigital.ecommerce.Repository.DocumentsRepository documentsRepository) {
        this.paymentRepository = paymentRepository;
        this.mailSender = mailSender;
        this.subscriptionRepository = subscriptionRepository;
        this.emailService = emailService;
        this.documentsRepository = documentsRepository;
    }

    public Boolean processPayment(PaymentSuscriptionDto paymentSuscriptionDto) throws Exception {
        log.info("Processing payment: {}", paymentSuscriptionDto);

        validatePayment(paymentSuscriptionDto);

        Payment payment = createPayment(paymentSuscriptionDto);

        if (payment.isSubscription()) {
            processSubscriptionPayment(paymentSuscriptionDto, payment);
        } else {
            processOrderPayment(paymentSuscriptionDto, payment);
        }

        paymentRepository.save(payment);
        return true;
    }

    private void validatePayment(PaymentSuscriptionDto paymentSuscriptionDto) throws Exception {
        if (paymentSuscriptionDto.getStatus() == null) {
            throw new Exception("Payment was not successful");
        }

        String guestEmail = paymentSuscriptionDto.getGuestEmail();
        if (guestEmail == null || guestEmail.isEmpty()) {
            throw new IllegalArgumentException("To address must not be null");
        }
    }

    private Payment createPayment(PaymentSuscriptionDto paymentSuscriptionDto) {
        Payment payment = new Payment();
        payment.setPaymentDate(new java.sql.Timestamp(System.currentTimeMillis()));
        payment.setUserId(paymentSuscriptionDto.getUserId() != null ? paymentSuscriptionDto.getUserId() : null);
        payment.setAmount(paymentSuscriptionDto.getAmount());
        payment.setPaymentStatus(paymentSuscriptionDto.getStatus());
        payment.setIsSubscription(paymentSuscriptionDto.isSubscription());
        return payment;
    }

    private void processSubscriptionPayment(PaymentSuscriptionDto paymentSuscriptionDto, Payment payment) {
        Subscription subscription = new Subscription();
        subscription.setUserId(paymentSuscriptionDto.getUserId());
        subscription.setStatus(paymentSuscriptionDto.getStatus());
        subscription.setSubscriptionType(paymentSuscriptionDto.getSubscriptionType());

        Date sqlStartDate = new Date(System.currentTimeMillis());
        subscription.setStartDate(sqlStartDate);

        LocalDate startLocalDate = sqlStartDate.toLocalDate();
        LocalDate endLocalDate = startLocalDate.plusMonths(12);
        Date sqlEndDate = Date.valueOf(endLocalDate);

        subscription.setEndDate(sqlEndDate);
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        payment.setSubscription(savedSubscription);

        log.info("Subscription successful: {}", subscription);
    }

    private void processOrderPayment(PaymentSuscriptionDto paymentSuscriptionDto, Payment payment) throws IOException, MessagingException {
        List<DocumentsEntity> documents = documentsRepository.findAllById(paymentSuscriptionDto.getDocumentIds());
        payment.setDocuments(documents);

        Payment savedPayment = paymentRepository.save(payment);

        String htmlContent = generatePaymentReceipt(savedPayment);
        String subject = paymentSuscriptionDto.getSubject();
        emailService.sendProductEmail(paymentSuscriptionDto.getGuestEmail(), subject, htmlContent);
    }

    public String generatePaymentReceipt(Payment payment) {
        String formattedDateTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payment.getPaymentDate());
        String amountPaid = String.valueOf(payment.getAmount());
        Long operationNumber = payment.getPaymentId();

        return "<html><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'><title></title></head><body style='color:black'>" +
                "<div style='width: 100%'>" +
                "<div style='display:flex;'></div>" +
                "<h1 style='margin-top: 2px ;text-align: center;font-weight: bold;font-style: italic;'>Voucher de Pago</h1>" +
                "<h2 style='text-align: center;'>Fecha de Pago :" + formattedDateTime + "  </h2>" +
                "<h2 style='text-align: center;'>Monto Pagado: $" + amountPaid + "  </h2>" +
                "<h2 style='text-align: center;'>Cod. de transacción :" + operationNumber + "  </h2>" +
                "<h2 style='text-align: center;'>¡Gracias por su pago!</h2>" +
                "<center><p style='margin-left: 10%;margin-right: 10%;'>Te saluda la familia Carpeta Digital</p></center> " +
                "<center><div style='width: 100%'></a></div></center>" +
                "<center><div style='width: 100%'><p style='margin-left: 10%;margin-right: 10%; '></p>" +
                "<center>Recuerde que el pago también lo puede realizar mediante depósito en nuestra cuenta corriente a través de Agente BCP, Agencia BCP o transferencia bancaria desde Banca por Internet.</center>" +
                "</div></center>" +
                "<center><div style='width: 100%'><p style='margin-left: 10%;margin-right: 10%; '>----------------------------</p></div></center>" +
                "</div></center>" +
                "</body></html>";
    }
}
