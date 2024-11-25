package ru.afbtest.calculator.utils;

import ru.afbtest.calculator.DTO.Enums.EmploymentStatus;
import ru.afbtest.calculator.DTO.Enums.Gender;
import ru.afbtest.calculator.DTO.Enums.MaritalStatus;
import ru.afbtest.calculator.DTO.Enums.Position;
import ru.afbtest.calculator.DTO.ScoringDataDto;

import java.math.BigDecimal;

public class Scoring {
    // TODO: Добавить исключения
    public static BigDecimal getEmploymentRate(EmploymentStatus status)  {
        switch (status) {
            case SELFEMPLOYED:
                return BigDecimal.valueOf(2);
            case EMPLOYED:
                return BigDecimal.ZERO;
            case BUSINESSOWNER:
                return BigDecimal.ONE;
            case UNEMPLOYED:
                System.out.println("Отказ безработному"); // нужно исключение
                //   throw new ScoreDenyedException("Denied by employment status");
            default:
                return  BigDecimal.valueOf(-77777);
                //   throw new ScoreDenyedException("Unknown Employed status");
        }
    }

    public static BigDecimal getPositionRate(Position position) {
        switch (position) {
            case MID_MANAGER:
                return new BigDecimal(-2);
            case TOP_MANAGER:
                return new BigDecimal(-3);
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
            default:
                return BigDecimal.ZERO;
        }
    }

    // TODO: Добавить исключения
    public static BigDecimal getGenderAndAgeRate(Gender gender, int age)  {
        if (age < 20 || age > 60)
            System.out.println("Отказ молодым и старым");
           // throw new ScoreDenyedException("The applicant does not meet the age requirements. Denied");
        switch (gender) {
            case MALE:
                return (age >= 30 && age <= 55) ? BigDecimal.valueOf(-3) : BigDecimal.ZERO;   // попрообовать через if
            case FEMALE:
                return (age >= 32 && age <= 60) ? BigDecimal.valueOf(-3) : BigDecimal.ZERO;
            case OTHER:
                return BigDecimal.valueOf(7);
            default:
               return  BigDecimal.valueOf(-77777);
              //  throw new ScoreDenyedException("Unknown Gender status");
        }
    }

    public static BigDecimal getWorkExperience(int workExperienceTotal, int workExperienceCurrent)  {
        if (workExperienceTotal < 18 || workExperienceCurrent < 3)
            System.out.println("Отказ по общему стажу");
        return null;
    }

}
