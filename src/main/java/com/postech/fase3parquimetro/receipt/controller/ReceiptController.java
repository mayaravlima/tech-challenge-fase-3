package com.postech.fase3parquimetro.receipt.controller;

import com.postech.fase3parquimetro.receipt.model.ReceiptReadRecord;
import com.postech.fase3parquimetro.receipt.service.ReceiptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceiptReadRecord> getReceiptById(@PathVariable String id) {
        return ResponseEntity.ok(receiptService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ReceiptReadRecord>> getAllReceipts() {
        return ResponseEntity.ok(receiptService.findAllReceipts());
    }
}
