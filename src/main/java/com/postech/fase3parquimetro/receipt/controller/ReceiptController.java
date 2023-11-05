package com.postech.fase3parquimetro.receipt.controller;

import com.postech.fase3parquimetro.receipt.service.ReceiptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @GetMapping("/{id}")
    public ResponseEntity getReceiptById(@PathVariable String id) {
        return ResponseEntity.ok(receiptService.getById(id));
    }

    @GetMapping
    public ResponseEntity getAllReceipts() {
        return ResponseEntity.ok(receiptService.findAllReceipts());
    }
}
