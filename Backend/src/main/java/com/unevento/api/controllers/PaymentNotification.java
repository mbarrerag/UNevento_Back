package com.unevento.api.controllers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mercadopago.MercadoPagoConfig;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.unevento.api.domain.records.PreferenceData;

@CrossOrigin
@RestController
@RequestMapping("/payment-notification")
public class PaymentNotification {

        @PostMapping("/payment-notification")
        public ResponseEntity<String> handleNotification() {

            return ResponseEntity.ok("LLego la notificacion de pago!");
        }
    }
