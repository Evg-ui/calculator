package ru.afbtest.calculator.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class PreScoring {

    public static final String passportSeries_REGEX = "^(\\d{4})";
    public static final String passportNumber_REGEX = "^(\\d{6})";
    public static final String firstName_REGEX = "[a-zA-z]{2,30}";
    public static final String lastName_REGEX = "[a-zA-z]{2,30}";
    public static final BigDecimal amount_MIN = BigDecimal.valueOf(20000);
    public static final Integer term_MIN = 6;
    public static final Long age_MIN = 18L;
    // public static LocalDate birthdate = null;
    public static final String email_REGEX = "^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$";


}
