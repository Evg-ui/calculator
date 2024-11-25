package ru.afbtest.calculator.notUsedYet;

import ru.afbtest.calculator.DTO.EmploymentDto;
import ru.afbtest.calculator.DTO.Enums.Gender;
import ru.afbtest.calculator.DTO.Enums.MaritalStatus;

import java.time.LocalDate;

public class FinishRegistrationRequestDto {
    private Gender gender;
    private MaritalStatus maritalStatus;
    private Integer dependentAmount;
    private LocalDate passportIssueDate;
    private String passportIssueBrach;
    private EmploymentDto employment;
    private String accountNumber;

}
