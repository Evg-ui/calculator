package ru.afbtest.calculator.DTO;

import ru.afbtest.calculator.DTO.Enums.EmploymentStatus;

import javax.swing.text.Position;
import java.math.BigDecimal;

public class EmploymentDto {
    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private Position position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;

}
