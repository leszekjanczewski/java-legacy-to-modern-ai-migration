package com.legacy.creditscoring.service;

import com.legacy.creditscoring.dto.CreditApplicationDTO;
import com.legacy.creditscoring.dto.CustomerDTO;
import com.legacy.creditscoring.entity.CreditApplication;
import com.legacy.creditscoring.entity.Customer;
import com.legacy.creditscoring.repository.CreditApplicationRepository;
import com.legacy.creditscoring.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

// LEGACY GOD CLASS - narusza Single Responsibility Principle
// Ten jeden serwis robi WSZYSTKO:
// 1. Orkiestracja persystencji (CRUD przez repozytoria)
// 2. Mapowanie DTO <-> Entity (powinien być osobny Mapper)
// 3. Algorytm scoringu kredytowego (powinna być osobna klasa ScoringCalculator)
// 4. Obliczanie grade (powinna być osobna klasa lub enum z logiką)
// 5. Obsługa błędów (RuntimeException bez hierarchii)
//
// W nowoczesnym kodzie to byłoby rozdzielone na:
// - CustomerService (CRUD)
// - CreditApplicationService (CRUD + workflow)
// - ScoringEngine (pure business logic)
// - CustomerMapper / ApplicationMapper (MapStruct)
@Service
public class CreditScoringService {

    // LEGACY: Field injection przez @Autowired zamiast constructor injection
    // Problemy:
    // 1. Pola nie są final - można je nadpisać po inicjalizacji
    // 2. Niemożliwe unit testy bez Spring contextu (lub refleksja/Mockito @InjectMocks)
    // 3. Ukrywa zależności - klasa wydaje się prostsza niż jest
    // 4. Circular dependency wykrywany dopiero w runtime, nie kompilacji
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CreditApplicationRepository creditApplicationRepository;

    // =========================================================
    // CUSTOMER OPERATIONS
    // =========================================================

    // LEGACY: Brak @Transactional na operacji zapisu
    // Jeśli save() rzuci wyjątek po częściowym wykonaniu - brak rollback
    public CustomerDTO createCustomer(CustomerDTO dto) {
        Customer customer = new Customer();

        // LEGACY: Verbose null check cascade zamiast Optional.ofNullable().orElse()
        // Nowoczesny kod: customer.setFirstName(Optional.ofNullable(dto.getFirstName()).orElse("UNKNOWN"))
        // Lub jeszcze lepiej: @NotNull na DTO + @Valid w kontrolerze
        if (dto.getFirstName() != null) {
            customer.setFirstName(dto.getFirstName());
        } else {
            customer.setFirstName("UNKNOWN");
        }

        if (dto.getLastName() != null) {
            customer.setLastName(dto.getLastName());
        } else {
            customer.setLastName("UNKNOWN");
        }

        if (dto.getAge() != null) {
            customer.setAge(dto.getAge());
        } else {
            customer.setAge(0);
        }

        if (dto.getMonthlyIncome() != null) {
            customer.setMonthlyIncome(dto.getMonthlyIncome());
        } else {
            customer.setMonthlyIncome(0.0);
        }

        if (dto.getEmploymentYears() != null) {
            customer.setEmploymentYears(dto.getEmploymentYears());
        } else {
            customer.setEmploymentYears(0);
        }

        if (dto.getCreditHistory() != null) {
            customer.setCreditHistory(dto.getCreditHistory());
        } else {
            customer.setCreditHistory("NONE");
        }

        // LEGACY: new Date() bezpośrednio w serwisie
        // Problemy:
        // 1. Niemożliwe do testowania (czas jest nieznany w teście)
        // 2. Brak abstrakcji zegara (Clock injection - wzorzec z Java 8)
        // 3. Nowoczesne podejście: wstrzyknięty Clock, then: Date.from(clock.instant())
        customer.setCreatedAt(new Date());

        Customer saved = customerRepository.save(customer);
        return mapCustomerToDTO(saved);
    }

    public CustomerDTO getCustomerById(Long id) {
        // LEGACY: findOne() zwraca null dla nieistniejącego ID
        // Spring Data 2.x (Boot 2.0+) zastąpił to findById() zwracającym Optional<T>
        // Nowoczesny kod: customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id))
        Customer customer = customerRepository.findOne(id);

        // LEGACY: Ręczny null check zamiast Optional
        if (customer == null) {
            // LEGACY: Rzucamy RuntimeException bez własnej hierarchii wyjątków
            // Propaguje jako HTTP 500 zamiast HTTP 404
            // Brak @ExceptionHandler / @ControllerAdvice który mapowałby to na właściwy status
            throw new RuntimeException("Customer not found with id: " + id);
        }

        return mapCustomerToDTO(customer);
    }

    // =========================================================
    // APPLICATION OPERATIONS
    // =========================================================

    public CreditApplicationDTO submitApplication(CreditApplicationDTO dto) {
        // LEGACY: Kolejny null check bez Optional
        if (dto.getCustomerId() == null) {
            throw new RuntimeException("Customer ID must not be null");
        }

        Customer customer = customerRepository.findOne(dto.getCustomerId());

        if (customer == null) {
            throw new RuntimeException("Customer not found with id: " + dto.getCustomerId());
        }

        CreditApplication application = new CreditApplication();
        application.setCustomer(customer);
        application.setRequestedAmount(dto.getRequestedAmount());
        application.setStatus("PENDING");
        application.setCreatedAt(new Date());

        // LEGACY: Scoring, grading i status - wszystko inline w tym samym serwisie
        // Brak delegacji do osobnej klasy odpowiedzialnej tylko za scoring
        int score = calculateScore(customer);
        application.setScore(score);

        String grade = calculateGrade(score);
        application.setGrade(grade);

        String status = calculateStatus(grade);
        application.setStatus(status);

        CreditApplication saved = creditApplicationRepository.save(application);
        return mapApplicationToDTO(saved);
    }

    public CreditApplicationDTO getApplicationById(Long id) {
        CreditApplication application = creditApplicationRepository.findOne(id);

        if (application == null) {
            throw new RuntimeException("Application not found with id: " + id);
        }

        return mapApplicationToDTO(application);
    }

    // =========================================================
    // SCORING LOGIC
    // =========================================================

    // LEGACY: Magic numbers bez named constants
    // Żeby zmienić próg dochodu z 5000 na 6000 trzeba przeczytać całą metodę
    // i zgadnąć która liczba odpowiada czemu. Brak:
    // private static final int INCOME_HIGH_THRESHOLD = 5000;
    // private static final int INCOME_HIGH_SCORE = 30;
    // etc.
    private int calculateScore(Customer customer) {
        int score = 0;

        // Czynnik wiekowy
        if (customer.getAge() != null) {
            if (customer.getAge() >= 25 && customer.getAge() <= 55) {
                score = score + 20;
            } else if (customer.getAge() >= 18 && customer.getAge() <= 24) {
                score = score + 10;
            } else if (customer.getAge() >= 56) {
                score = score + 15;
            } else {
                // Wiek < 18 - 0 punktów
                score = score + 0;
            }
        }

        // Czynnik dochodu
        if (customer.getMonthlyIncome() != null) {
            if (customer.getMonthlyIncome() > 5000) {
                score = score + 30;
            } else if (customer.getMonthlyIncome() >= 3000) {
                score = score + 20;
            } else if (customer.getMonthlyIncome() >= 1000) {
                score = score + 10;
            }
            // < 1000 - 0 punktów
        }

        // Czynnik stażu pracy
        if (customer.getEmploymentYears() != null) {
            if (customer.getEmploymentYears() > 5) {
                score = score + 25;
            } else if (customer.getEmploymentYears() >= 2) {
                score = score + 15;
            } else {
                score = score + 5;
            }
        }

        // Czynnik historii kredytowej
        // LEGACY: String.equals() zamiast switch(enum) - ryzyko literówki, brak kompilatora
        if (customer.getCreditHistory() != null) {
            if (customer.getCreditHistory().equals("GOOD")) {
                score = score + 25;
            } else if (customer.getCreditHistory().equals("BAD")) {
                score = score - 20;
            } else if (customer.getCreditHistory().equals("NONE")) {
                score = score + 0;
            }
            // LEGACY: Brak "else" dla nieznanych wartości - cicha ignorancja błędnych danych
        }

        // LEGACY: Clampowanie bez dedykowanej metody clamp(value, min, max)
        // Math.min/max byłoby lepsze, ale przynajmniej działa
        if (score < 0) {
            score = 0;
        }
        if (score > 100) {
            score = 100;
        }

        return score;
    }

    private String calculateGrade(int score) {
        // LEGACY: Magic numbers, zwraca raw String zamiast enum Grade
        if (score >= 80) {
            return "A";
        } else if (score >= 60) {
            return "B";
        } else if (score >= 40) {
            return "C";
        } else if (score >= 20) {
            return "D";
        } else {
            return "F";
        }
    }

    private String calculateStatus(String grade) {
        // LEGACY: String comparison zamiast enum switch
        if (grade.equals("A") || grade.equals("B")) {
            return "APPROVED";
        } else {
            return "REJECTED";
        }
    }

    // =========================================================
    // MAPPING - powinna być osobna klasa Mapper (np. z MapStruct)
    // =========================================================

    private CustomerDTO mapCustomerToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setAge(customer.getAge());
        dto.setMonthlyIncome(customer.getMonthlyIncome());
        dto.setEmploymentYears(customer.getEmploymentYears());
        dto.setCreditHistory(customer.getCreditHistory());

        // LEGACY: SimpleDateFormat tworzony per wywołanie
        // Problemy:
        // 1. SimpleDateFormat jest NOT thread-safe (mutable state)
        // 2. Tworzenie nowego obiektu per wywołanie = garbage collection pressure
        // 3. Nowoczesny kod: DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") (immutable, thread-safe)
        if (customer.getCreatedAt() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dto.setCreatedAt(sdf.format(customer.getCreatedAt()));
        } else {
            dto.setCreatedAt("");
        }

        return dto;
    }

    private CreditApplicationDTO mapApplicationToDTO(CreditApplication application) {
        CreditApplicationDTO dto = new CreditApplicationDTO();
        dto.setId(application.getId());

        // LEGACY: Nested null check zamiast Optional chaining
        // Nowoczesny: Optional.ofNullable(application.getCustomer()).map(Customer::getId).ifPresent(dto::setCustomerId)
        if (application.getCustomer() != null) {
            dto.setCustomerId(application.getCustomer().getId());
        }

        dto.setRequestedAmount(application.getRequestedAmount());
        dto.setScore(application.getScore());
        dto.setGrade(application.getGrade());
        dto.setStatus(application.getStatus());

        if (application.getCreatedAt() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dto.setCreatedAt(sdf.format(application.getCreatedAt()));
        } else {
            dto.setCreatedAt("");
        }

        return dto;
    }
}
