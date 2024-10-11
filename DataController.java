package com.yourcompany.ccards.controller;

import com.yourcompany.ccards.service.DataGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/data")
public class DataController {

    @Autowired
    private DataGenerationService dataGenerationService;

    @GetMapping("/generate/{count}")
    public ResponseEntity<String> generateData(@PathVariable int count) {
        dataGenerationService.generateData(count);
        return ResponseEntity.ok("Generated data: " + dataGenerationService.getDataCounts());
    }

    @GetMapping("/count")
    public ResponseEntity<String> getDataCounts() {
        return ResponseEntity.ok(dataGenerationService.getDataCounts());
    }

    @GetMapping("/count/cardholders")
    public ResponseEntity<String> getCardholderCount() {
        return ResponseEntity.ok("Cardholder count: " + dataGenerationService.getCardholderCount());
    }

    @GetMapping("/count/cardcreations")
    public ResponseEntity<String> getCardCreationCount() {
        return ResponseEntity.ok("Card Creation count: " + dataGenerationService.getCardCreationCount());
    }
}
