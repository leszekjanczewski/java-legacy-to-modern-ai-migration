package com.legacy.creditscoring.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

// LEGACY: Brak @Data, @Builder, @NoArgsConstructor z Lombok
// Wszystko ręcznie
@Entity
@Table(name = "customer")
public class Customer {

    // LEGACY: GenerationType.AUTO zamiast IDENTITY - różne zachowanie na różnych DB/Hibernate
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // LEGACY: Brak @Column(nullable = false) - brak constraints na poziomie DB
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "age")
    private Integer age;  // LEGACY: Integer (boxed) zamiast int - ryzyko NPE

    @Column(name = "monthly_income")
    private Double monthlyIncome;  // LEGACY: Double zamiast BigDecimal - błędy zaokrąglania

    @Column(name = "employment_years")
    private Integer employmentYears;

    // LEGACY: String zamiast enum z @Enumerated(EnumType.STRING) - brak type safety
    // Możliwe wartości: "GOOD", "BAD", "NONE" - ale nic tego nie wymusza
    @Column(name = "credit_history")
    private String creditHistory;

    // LEGACY: java.util.Date + @Temporal zamiast java.time.LocalDateTime (Java 8+)
    // LocalDateTime z Hibernate 5.2+ nie wymaga @Temporal
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    // LEGACY: Wymagany konstruktor no-arg przez JPA/Hibernate
    public Customer() {
    }

    // LEGACY: Verbose gettery/settery zamiast Lombok @Data
    // ~40 linii boilerplate dla prostej encji

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(Double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public Integer getEmploymentYears() {
        return employmentYears;
    }

    public void setEmploymentYears(Integer employmentYears) {
        this.employmentYears = employmentYears;
    }

    public String getCreditHistory() {
        return creditHistory;
    }

    public void setCreditHistory(String creditHistory) {
        this.creditHistory = creditHistory;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
