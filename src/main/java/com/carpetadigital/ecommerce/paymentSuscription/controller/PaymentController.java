package com.carpetadigital.ecommerce.paymentSuscription.controller;




import com.carpetadigital.ecommerce.entity.Payment;
import com.carpetadigital.ecommerce.entity.dto.PaymentSuscriptionDto;
import com.carpetadigital.ecommerce.paymentSuscription.service.PaymentService;
import com.carpetadigital.ecommerce.utils.handler.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;



    @PostMapping
    public ResponseEntity<?> makePayment(@RequestBody PaymentSuscriptionDto paymentSuscriptionDto) throws Exception {
        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                paymentService.processPayment(paymentSuscriptionDto),
                true
        );


    }

}

