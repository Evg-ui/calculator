package ru.afbtest.calculator;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.afbtest.calculator.DTO.Enums.MaritalStatus;
import ru.afbtest.calculator.DTO.LoanOfferDto;
import ru.afbtest.calculator.DTO.LoanStatementRequestDto;
import ru.afbtest.calculator.DTO.PaymentScheduleElementDto;
import ru.afbtest.calculator.DTO.ScoringDataDto;
import ru.afbtest.calculator.exception.ScoreException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class CalculatorApplication {

    public static void main(String[] args) throws ScoreException {

        //   SpringApplication.run(CalculatorApplication.class, args);

        // проверка прескоринга
        CalculatorService calculatorService = new CalculatorService();
        LoanStatementRequestDto requestDto = new LoanStatementRequestDto();
        requestDto.setFirstName("Evgeniya");
        requestDto.setLastName("Berezentseva");
        requestDto.setMiddleName("Vladimirovna");
        requestDto.setAmount(BigDecimal.valueOf(300000));
        requestDto.setTerm(6);
        requestDto.setBirthDate(LocalDate.parse("2024-12-01"));
        requestDto.setEmail("mail.123@example.com");
        requestDto.setPassportSeries("1255");
        requestDto.setPassportNumber("56700");
        calculatorService.preScoringCheck(requestDto);
        System.out.println(calculatorService.preScoringCheck(requestDto));

        // проверка расчета ежемесячного платежа
        BigDecimal amount = BigDecimal.valueOf(300000);
        BigDecimal rate = BigDecimal.valueOf(20);
        Integer term = 6;
        BigDecimal result = calculatorService.calcMonthlyPayment(amount, rate, term);
        System.out.println("Ежемесячный  платеж составляет: " + calculatorService.calcMonthlyPayment(amount, rate, term));

        // проверка получения предложений
        List<LoanOfferDto> loanOffers = calculatorService.getLoanOffers(requestDto);
        for (LoanOfferDto loanOffer : loanOffers) {
            System.out.println(loanOffer);
        }

        // проверка графика платежей
        List<PaymentScheduleElementDto> payElements = calculatorService.calcPaymentSchedule(
                  (BigDecimal.valueOf(300000))
                ,6
                , BigDecimal.valueOf(20)
                , calculatorService.calcMonthlyPayment(amount, rate, term)
        );
        System.out.println("График платежей: ");
        for (PaymentScheduleElementDto pElement : payElements) {
            System.out.println(pElement);
        }

//        // проверка скоринга
//        ScoringDataDto scoringDataDto = new ScoringDataDto();
//        scoringDataDto.setBirthdate(LocalDate.parse("2001-12-01"));
//        scoringDataDto.setMaritalStatus(MaritalStatus.MARRIED);
//        calculatorService.scoringCheck(scoringDataDto);
    }

}
