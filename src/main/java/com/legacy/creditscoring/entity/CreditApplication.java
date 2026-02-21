package com.legacy.creditscoring.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "credit_application")
public class CreditApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // LEGACY: Brak fetch = FetchType.LAZY
    // Default dla @ManyToOne to EAGER - klasyczny N+1 problem
    // Każde pobranie listy aplikacji = dodatkowe SELECT per customer
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "requested_amount")
    private Double requestedAmount;  // LEGACY: Double zamiast BigDecimal

    @Column(name = "score")
    private Integer score;  // 0-100

    // LEGACY: String zamiast enum - możliwe wartości: "A", "B", "C", "D", "F"
    @Column(name = "grade")
    private String grade;

    // LEGACY: String zamiast enum - możliwe wartości: "PENDING", "APPROVED", "REJECTED"
    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;  // LEGACY: java.util.Date

    public CreditApplication() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Double getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(Double requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
