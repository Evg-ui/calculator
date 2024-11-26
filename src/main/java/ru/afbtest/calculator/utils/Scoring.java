package ru.afbtest.calculator.utils;

import ru.afbtest.calculator.DTO.Enums.EmploymentStatus;
import ru.afbtest.calculator.DTO.Enums.Gender;
import ru.afbtest.calculator.DTO.Enums.MaritalStatus;
import ru.afbtest.calculator.DTO.Enums.Position;
import ru.afbtest.calculator.exception.ScoreException;

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
                return (age >= 30 && age <= 55) ? BigDecimal.valueOf(-3) : BigDecimal.ZERO;   // попрообовать через if
            case FEMALE:
                return (age >= 32 && age <= 60) ? BigDecimal.valueOf(-3) : BigDecimal.ZERO;
            case OTHER:
                return BigDecimal.valueOf(7);
            default:
              throw new ScoreException("Неизвестный пол");
        }
    }

//    public static BigDecimal getWorkExperience(int workExperienceTotal, int workExperienceCurrent) throws ScoreException {
//        if (workExperienceTotal < 18 || workExperienceCurrent < 3) {
//            throw new ScoreException("Стаж не соответствует установленным ограничениям.");
//        } else {
//            return BigDecimal.ZERO;
//        }
//    }

}
