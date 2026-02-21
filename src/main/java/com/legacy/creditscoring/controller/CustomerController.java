package com.legacy.creditscoring.controller;

import com.legacy.creditscoring.dto.CustomerDTO;
import com.legacy.creditscoring.service.CreditScoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// LEGACY Anti-patterns w tym kontrolerze:
// 1. @Autowired field injection zamiast constructor injection
// 2. Zwraca raw CustomerDTO zamiast ResponseEntity<CustomerDTO>
//    -> POST zawsze zwraca HTTP 200 zamiast 201 Created
// 3. Brak @Valid na @RequestBody - żadna walidacja nie jest wykonywana
// 4. Brak @ExceptionHandler / @ControllerAdvice
//    -> RuntimeException z serwisu propaguje jako HTTP 500 z domyślnym Spring error body
//    -> "Customer not found" daje 500, a powinno dać 404
// 5. Brak HATEOAS (linki do powiązanych zasobów)
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    // LEGACY: Field injection
    @Autowired
    private CreditScoringService creditScoringService;

    @PostMapping
    public CustomerDTO createCustomer(@RequestBody CustomerDTO customerDTO) {
        // LEGACY: Brak @Valid - nikt nie waliduje że age > 0, monthlyIncome >= 0, etc.
        // LEGACY: Zwraca 200 OK zamiast 201 Created (brak ResponseEntity.status(201).body(...))
        return creditScoringService.createCustomer(customerDTO);
    }

    @GetMapping("/{id}")
    public CustomerDTO getCustomer(@PathVariable Long id) {
        // LEGACY: RuntimeException z serwisu -> HTTP 500
        // Nowoczesny: @ExceptionHandler(CustomerNotFoundException.class) -> 404
        return creditScoringService.getCustomerById(id);
    }
}
