package ru.berezentseva.calculator.utils;

import ru.berezentseva.calculator.DTO.Enums.EmploymentStatus;
import ru.berezentseva.calculator.DTO.Enums.Gender;
import ru.berezentseva.calculator.DTO.Enums.MaritalStatus;
import ru.berezentseva.calculator.DTO.Enums.Position;
import ru.berezentseva.calculator.exception.ScoreException;

import java.math.BigDecimal;

public class Scoring {

    public static BigDecimal getEmploymentRate(EmploymentStatus status) throws ScoreException {
        switch (status) {
            case SELFEMPLOYED:
                return BigDecimal.valueOf(2);
            case EMPLOYED:
                return BigDecimal.ZERO;
            case BUSINESSOWNER:
                return BigDecimal.ONE;
            case UNEMPLOYED:
                   throw new ScoreException("Отказ по статусу \"Безработный\"");
            default:
                   throw new ScoreException("Неизвестный статус");
        }
    }

    public static BigDecimal getPositionRate(Position position) {
        switch (position) {
            case MID_MANAGER:
                return new BigDecimal(-2);
            case TOP_MANAGER:
                return new BigDecimal(-3);
            case OWNER:
                return new BigDecimal(2);
            default:
                return BigDecimal.ZERO;
        }
    }

    public static BigDecimal getMaritalStatusRate(MaritalStatus maritalStatus) {
        switch (maritalStatus) {
            case MARRIED:
                return new BigDecimal(-3);
            case DIVORCED:
                return BigDecimal.ONE;
            case WIDOWED:
                return new BigDecimal(-1);
            default:
                return BigDecimal.ZERO;
        }
    }

    public static BigDecimal getGenderAndAgeRate(Gender gender, int age) throws ScoreException {
            switch (gender) {
            case MALE:
                return (age >= 30 && age <= 55) ? BigDecimal.valueOf(-3) : BigDecimal.ZERO;
            case FEMALE:
                return (age >= 32 && age <= 60) ? BigDecimal.valueOf(-3) : BigDecimal.ZERO;
            case OTHER:
                return BigDecimal.valueOf(7);
            default:
              throw new ScoreException("Неизвестный пол");
        }
    }
}
