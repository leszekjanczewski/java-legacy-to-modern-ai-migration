package com.legacy.creditscoring.dto;

// LEGACY: DTO jako request i response w jednym (brak rozdzielenia na Request/Response DTO)
// Brak walidacji, brak @JsonProperty, brak Lombok
public class CreditApplicationDTO {

    private Long id;
    private Long customerId;        // Tylko ID klienta, nie cały Customer (przynajmniej to dobrze)
    private Double requestedAmount;
    private Integer score;          // Wypełniane przez serwis podczas scoringu
    private String grade;           // Wypełniane przez serwis - "A", "B", "C", "D", "F"
    private String status;          // Wypełniane przez serwis - "APPROVED", "REJECTED"
    private String createdAt;       // LEGACY: String zamiast Date/LocalDateTime

    public CreditApplicationDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
