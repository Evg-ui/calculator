package ru.berezentseva.calculator;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.berezentseva.calculator.DTO.LoanOfferDto;
import ru.berezentseva.calculator.DTO.LoanStatementRequestDto;
import ru.berezentseva.calculator.DTO.PaymentScheduleElementDto;
import ru.berezentseva.calculator.DTO.ScoringDataDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        //      CalculatorService calculatorService = new CalculatorService();

//       LoanStatementRequestDto requestDto = new LoanStatementRequestDto();
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
//        BigDecimal amount = BigDecimal.valueOf(300000);
//        BigDecimal rate = BigDecimal.valueOf(20);
//        Integer term = 6;
//        BigDecimal result = calculatorService.calcMonthlyPayment(amount, rate, term);
//        System.out.println("Ежемесячный  платеж составляет: " + result);

//        // проверка получения предложений
//       List<LoanOfferDto> loanOffers = calculatorService.getLoanOffers(requestDto);
//        for (LoanOfferDto loanOffer : loanOffers) {
//            System.out.println(loanOffer);
//        }
//
//        // проверка графика платежей
//        List<PaymentScheduleElementDto> payElements = calculatorService.calcPaymentSchedule(
//                  (BigDecimal.valueOf(300000))
//                ,6
//                , BigDecimal.valueOf(15)
//                , calculatorService.calcMonthlyPayment(BigDecimal.valueOf(300000), BigDecimal.valueOf(15), 6)
//        );
//        System.out.println("График платежей: ");
//        for (PaymentScheduleElementDto pElement : payElements) {
//            System.out.println(pElement);
//        }

        //     System.out.println(calculatorService.calcMonthlyPayment(BigDecimal.valueOf(300000), BigDecimal.valueOf(17),6));

        // проверка расчета ПСК
//        BigDecimal amountForPsk = BigDecimal.valueOf(300000);
//        Integer termForPsk = 6;
//        List<PaymentScheduleElementDto> paymentScheduleForPsk = new ArrayList<>();
//
//        paymentScheduleForPsk.add(new PaymentScheduleElementDto(1,
//                LocalDate.now(),
//                BigDecimal.valueOf(52210.14), // ежем платеж
//                BigDecimal.valueOf(3698.63), // %
//                BigDecimal.valueOf(48511.51), // осн долг
//                BigDecimal.valueOf(251488.49))); // остаток 1 мес
//        paymentScheduleForPsk.add(new PaymentScheduleElementDto(2,
//                LocalDate.now(),
//                BigDecimal.valueOf(52210.14), // ежем платеж
//                BigDecimal.valueOf(3203.89), // %
//                BigDecimal.valueOf(49006.25), // осн долг
//                BigDecimal.valueOf(202482.24)));   // остаток 2 мес
//        paymentScheduleForPsk.add(new PaymentScheduleElementDto(2,
//                LocalDate.now(),
//                BigDecimal.valueOf(52210.14), // ежем платеж
//                BigDecimal.valueOf(2579.56), // %
//                BigDecimal.valueOf(49630.58), // осн долг
//                BigDecimal.valueOf(152851.66)));   // остаток 3 мес
//        paymentScheduleForPsk.add(new PaymentScheduleElementDto(2,
//                LocalDate.now(),
//                BigDecimal.valueOf(52210.14), // ежем платеж
//                BigDecimal.valueOf(1758.84), // %
//                BigDecimal.valueOf(50451.30), // осн долг
//                BigDecimal.valueOf(102400.36)));   // остаток 4 мес
//        paymentScheduleForPsk.add(new PaymentScheduleElementDto(2,
//                LocalDate.now(),
//                BigDecimal.valueOf(52210.14), // ежем платеж
//                BigDecimal.valueOf(1304.55), // %
//                BigDecimal.valueOf(51494.77), // осн долг
//                BigDecimal.valueOf(8354.07)));   // остаток 5 мес
//        paymentScheduleForPsk.add(new PaymentScheduleElementDto(2,
//                LocalDate.now(),
//                BigDecimal.valueOf(52210.14), // ежем платеж
//                BigDecimal.valueOf(634.86), // %
//                BigDecimal.valueOf(52129.63), // осн долг
//                BigDecimal.valueOf(8354.07)));   // остаток 6 мес
//
//        BigDecimal resultForPsk = calculatorService.calcPsk(BigDecimal.valueOf(300000),
//                6,
//                paymentScheduleForPsk);
//        System.out.println("ПСК: "  + resultForPsk);
//
//    }


        // проверка скоринга
//        ScoringDataDto scoringDataDto = new ScoringDataDto();
//        scoringDataDto.setBirthdate(LocalDate.parse("2001-12-01"));
//        scoringDataDto.setMaritalStatus(MaritalStatus.MARRIED);
//        calculatorService.scoringCheck(scoringDataDto);

    }
}




