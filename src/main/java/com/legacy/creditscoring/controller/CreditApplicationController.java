package com.legacy.creditscoring.controller;

import com.legacy.creditscoring.dto.CreditApplicationDTO;
import com.legacy.creditscoring.service.CreditScoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// LEGACY: Te same anty-patterny co w CustomerController:
// - Field injection
// - Brak ResponseEntity (zawsze HTTP 200 lub 500)
// - Brak @Valid
// - Brak ControllerAdvice
@RestController
@RequestMapping("/api/applications")
public class CreditApplicationController {

    @Autowired
    private CreditScoringService creditScoringService;

    @PostMapping
    public CreditApplicationDTO submitApplication(@RequestBody CreditApplicationDTO applicationDTO) {
        // Scoring odbywa się synchronicznie w serwisie - brak async, brak queue
        // LEGACY: Zwraca 200 zamiast 201
        return creditScoringService.submitApplication(applicationDTO);
    }

    @GetMapping("/{id}")
    public CreditApplicationDTO getApplication(@PathVariable Long id) {
        return creditScoringService.getApplicationById(id);
    }
}
