package com.legacy.creditscoring.repository;

import com.legacy.creditscoring.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

// LEGACY: Intentionally empty - legacy projekty wrzucały logikę zapytań
// bezpośrednio do serwisu (jako JPQL Stringi) lub używały EntityManager ręcznie.
// Brak custom queries, brak paginacji, brak projekcji (Projection interfaces).
// findAll() w serwisie ładuje WSZYSTKO do pamięci.
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
