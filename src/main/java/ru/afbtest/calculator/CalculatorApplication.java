package ru.afbtest.calculator;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.afbtest.calculator.DTO.Enums.MaritalStatus;
import ru.afbtest.calculator.DTO.LoanOfferDto;
import ru.afbtest.calculator.DTO.LoanStatementRequestDto;
import ru.afbtest.calculator.DTO.PaymentScheduleElementDto;
import ru.afbtest.calculator.DTO.ScoringDataDto;
import ru.afbtest.calculator.exception.ScoreException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Микросервис Кредитный калькулятор",
                description = "Прескоринг и скоринг кредита",
                version = "v1"
        )
)
public class CalculatorApplication {

    public static void main(String[] args) {

        SpringApplication.run(CalculatorApplication.class, args);

        // проверка прескоринга
//        CalculatorService calculatorService = new CalculatorService();
//        LoanStatementRequestDto requestDto = new LoanStatementRequestDto();
//        requestDto.setFirstName("Evgeniya");
//        requestDto.setLastName("Berezentseva");
//        requestDto.setMiddleName("Vladimirovna");
//        requestDto.setAmount(BigDecimal.valueOf(300000));
//        requestDto.setTerm(6);
//        requestDto.setBirthDate(LocalDate.parse("2000-12-01"));
//        requestDto.setEmail("mail.123@example.com");
//        requestDto.setPassportSeries("1255");
//        requestDto.setPassportNumber("567050");
//        calculatorService.preScoringCheck(requestDto);

        // проверка расчета ежемесячного платежа
       /* BigDecimal amount = BigDecimal.valueOf(300000);
        BigDecimal rate = BigDecimal.valueOf(20);
        Integer term = 6;
        BigDecimal result = calculatorService.calcMonthlyPayment(amount, rate, term);
        System.out.println("Ежемесячный  платеж составляет: " + result);

        // проверка получения предложений
//        List<LoanOfferDto> loanOffers = calculatorService.getLoanOffers(requestDto);
//        for (LoanOfferDto loanOffer : loanOffers) {
//            System.out.println(loanOffer);
//        }

        // проверка графика платежей
        List<PaymentScheduleElementDto> payElements = calculatorService.calcPaymentSchedule(
                  (BigDecimal.valueOf(300000))
                ,6
                , BigDecimal.valueOf(20)
                , calculatorService.calcMonthlyPayment(BigDecimal.valueOf(300000), BigDecimal.valueOf(20), 6)
        );
        System.out.println("График платежей: ");
        for (PaymentScheduleElementDto pElement : payElements) {
            System.out.println(pElement);
        }

        // проверка расчета ПСК
        BigDecimal amountForPsk = BigDecimal.valueOf(300000);
        Integer termForPsk = 6;
        List<PaymentScheduleElementDto> paymentScheduleForPsk = new ArrayList<>();

        paymentScheduleForPsk.add(new PaymentScheduleElementDto(1,
                LocalDate.now(),
                BigDecimal.valueOf(846.94),
                BigDecimal.valueOf(25),
                BigDecimal.valueOf(821.94),
                BigDecimal.valueOf(9178.06))); // Платеж за 1-й месяц
        paymentScheduleForPsk.add(new PaymentScheduleElementDto(1,
                LocalDate.now(),
                BigDecimal.valueOf(846.94),
                BigDecimal.valueOf(22.95),
                BigDecimal.valueOf(823.99),
                BigDecimal.valueOf(8354.07))); // Платеж за 2-й месяц

        BigDecimal resultForPsk = calculatorService.calcPsk(amountForPsk,
                termForPsk,
                paymentScheduleForPsk);
        System.out.println("ПСК: "  + resultForPsk);

    }


//        // проверка скоринга
//        ScoringDataDto scoringDataDto = new ScoringDataDto();
//        scoringDataDto.setBirthdate(LocalDate.parse("2001-12-01"));
//        scoringDataDto.setMaritalStatus(MaritalStatus.MARRIED);
//        calculatorService.scoringCheck(scoringDataDto);*/

    }
}


