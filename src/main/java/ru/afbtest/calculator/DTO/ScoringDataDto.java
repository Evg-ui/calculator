package ru.afbtest.calculator.DTO;

import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ScoringDataDto {
    private BigDecimal amount;
    private Integer term;
    private String firstName;
    private String lastName;
   // private Gender gender;
    private LocalDate birthdate;
    private String passportSeries;
    private String passportNumber;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
   // private maritalStatus maritalStatus;
    private Integer dependentAmount;
 //   private EmploymentDto employment;
    private String accountNumber;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;


}