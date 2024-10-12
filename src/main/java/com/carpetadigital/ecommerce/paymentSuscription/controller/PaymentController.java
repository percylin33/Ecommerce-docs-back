package com.carpetadigital.ecommerce.paymentSuscription.controller;




import com.carpetadigital.ecommerce.entity.Payment;
import com.carpetadigital.ecommerce.entity.dto.PaymentSuscriptionDto;
import com.carpetadigital.ecommerce.paymentSuscription.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;



    @PostMapping
    public String makePayment(@RequestBody PaymentSuscriptionDto paymentSuscriptionDto) {
        paymentService.processPayment(paymentSuscriptionDto);
        log.info("" + paymentSuscriptionDto);
        return "Pago procesado exitosamente";
    }

}

