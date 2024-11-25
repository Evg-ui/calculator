package ru.afbtest.calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.afbtest.calculator.DTO.LoanOfferDto;
import ru.afbtest.calculator.DTO.LoanStatementRequestDto;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootApplication
public class CalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalculatorApplication.class, args);
        // проверка прескоринга
     /*   CalculatorService preScoringService = new CalculatorService();
        LoanStatementRequestDto requestDto = new LoanStatementRequestDto();
        requestDto.setFirstName("Evgeniya");
        requestDto.setLastName("Berezentseva");
        requestDto.setMiddleName("Vladimirovna");
        requestDto.setAmount(BigDecimal.valueOf(100000));
        requestDto.setTerm(6);
        requestDto.setBirthDate(LocalDate.parse("2024-12-01"));
        requestDto.setEmail("mail.123@example.com");
        requestDto.setPassportSeries("1255");
        requestDto.setPassportNumber("567900");
        preScoringService.preScoringCheck(requestDto);
        System.out.println(preScoringService.preScoringCheck(requestDto));

        // проверка расчета ежемесячного платежа
        CalculatorService monthlyPayment = new CalculatorService();
        BigDecimal rate = BigDecimal.valueOf(25);
        BigDecimal amount = BigDecimal.valueOf(100000);
        Integer term = 30;
        CalculatorService.calcMonthlyPayment(amount, rate, term);
        System.out.println("Ежемесячный  платеж составляет: " + CalculatorService.calcMonthlyPayment(amount, rate, term));
*/


    }

}
