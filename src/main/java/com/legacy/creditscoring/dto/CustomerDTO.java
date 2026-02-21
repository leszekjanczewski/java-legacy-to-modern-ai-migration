package com.legacy.creditscoring.dto;

// LEGACY: Plain DTO bez:
// - @JsonProperty (polega na JavaBean naming conventions)
// - @NotNull, @Min, @Max (brak Bean Validation)
// - Builder pattern (Joshua Bloch "Effective Java" 2008, ale legacy tego nie używa)
// - Record (Java 16+)
// createdAt jako String - ręczna konwersja Date->String w serwisie przez SimpleDateFormat
public class CustomerDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private Double monthlyIncome;
    private Integer employmentYears;
    private String creditHistory;  // "GOOD", "BAD", "NONE"
    private String createdAt;      // LEGACY: Date przekonwertowany do String (nie ISO-8601 automatycznie)

    public CustomerDTO() {
    }

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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
