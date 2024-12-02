package ru.berezentseva.calculator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.berezentseva.calculator.DTO.*;
import ru.berezentseva.calculator.DTO.Enums.Gender;
import ru.berezentseva.calculator.DTO.Enums.MaritalStatus;
import ru.berezentseva.calculator.exception.ScoreException;
import ru.berezentseva.calculator.utils.Scoring;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class CalculatorServiceTest {

    int baseRate = 15;

    @InjectMocks
    private CalculatorService calculatorService;

    @BeforeEach
    public void setup() {
        calculatorService = new CalculatorService();
    }

    @Test
    void calcMonthlyPaymentTest() {
        // здесь проверяем, что рассчитанный на стороннем ресурсе платеж совпадет с нашим расчетом
        BigDecimal amount = BigDecimal.valueOf(300000);
        int term = 6;
        BigDecimal rate = BigDecimal.valueOf(15);
        BigDecimal monthlyPayment = calculatorService.calcMonthlyPayment(amount, rate, term);
        // при данных значениях ежемесячный платеж = 52210  его и ожидаем
        assertEquals(BigDecimal.valueOf(52210), monthlyPayment.setScale(0, BigDecimal.ROUND_HALF_UP));
    }

    @Test
    void CreditDtoSetterTest() {
        // здесь мы будем проверять, что заданные значения корректно попадают в сущность Кредит
        BigDecimal amount = new BigDecimal("300000");
        Integer term = 12;
        BigDecimal monthlyPayment = new BigDecimal("52210");
        BigDecimal rate = new BigDecimal("15");
        BigDecimal psk = new BigDecimal("10");
        Boolean isInsuranceEnabled = true;
        Boolean isSalaryClient = false;
        List<PaymentScheduleElementDto> paymentSchedule = new ArrayList<>();

        CreditDto creditDto = new CreditDto();
        creditDto.setAmount(amount);
        creditDto.setTerm(term);
        creditDto.setMonthlyPayment(monthlyPayment);
        creditDto.setRate(rate);
        creditDto.setPsk(psk);
        creditDto.setIsInsuranceEnabled(isInsuranceEnabled);
        creditDto.setIsSalaryClient(isSalaryClient);
        creditDto.setPaymentSchedule(paymentSchedule);

        Assertions.assertEquals(amount, creditDto.getAmount());
        Assertions.assertEquals(term, creditDto.getTerm());
        Assertions.assertEquals(monthlyPayment, creditDto.getMonthlyPayment());
        Assertions.assertEquals(rate, creditDto.getRate());
        Assertions.assertEquals(psk, creditDto.getPsk());
        Assertions.assertEquals(isInsuranceEnabled, creditDto.getIsInsuranceEnabled());
        Assertions.assertEquals(isSalaryClient, creditDto.getIsSalaryClient());
        Assertions.assertEquals(paymentSchedule, creditDto.getPaymentSchedule());
    }

    @Test
    void calcPskTest() {
        // проверяем расчет ПСК
        BigDecimal amount = BigDecimal.valueOf(300000); // Сумма кредита
        Integer term = 6; // Срок кредита в месяцах
        List<PaymentScheduleElementDto> paymentSchedule = new ArrayList<>();

        paymentSchedule.add(new PaymentScheduleElementDto(1,
                LocalDate.now(),
                BigDecimal.valueOf(52210.14), // ежем платеж
                BigDecimal.valueOf(3698.63), // %
                BigDecimal.valueOf(48511.51), // осн долг
                BigDecimal.valueOf(251488.49))); // остаток 1 мес
        paymentSchedule.add(new PaymentScheduleElementDto(2,
                LocalDate.now(),
                BigDecimal.valueOf(52210.14), // ежем платеж
                BigDecimal.valueOf(3203.89), // %
                BigDecimal.valueOf(49006.25), // осн долг
                BigDecimal.valueOf(202482.24)));   // остаток 2 мес
        paymentSchedule.add(new PaymentScheduleElementDto(2,
                LocalDate.now(),
                BigDecimal.valueOf(52210.14), // ежем платеж
                BigDecimal.valueOf(2579.56), // %
                BigDecimal.valueOf(49630.58), // осн долг
                BigDecimal.valueOf(152851.66)));   // остаток 3 мес
        paymentSchedule.add(new PaymentScheduleElementDto(2,
                LocalDate.now(),
                BigDecimal.valueOf(52210.14), // ежем платеж
                BigDecimal.valueOf(1758.84), // %
                BigDecimal.valueOf(50451.30), // осн долг
                BigDecimal.valueOf(102400.36)));   // остаток 4 мес
        paymentSchedule.add(new PaymentScheduleElementDto(2,
                LocalDate.now(),
                BigDecimal.valueOf(52210.14), // ежем платеж
                BigDecimal.valueOf(1304.55), // %
                BigDecimal.valueOf(51494.77), // осн долг
                BigDecimal.valueOf(8354.07)));   // остаток 5 мес
        paymentSchedule.add(new PaymentScheduleElementDto(2,
                LocalDate.now(),
                BigDecimal.valueOf(52210.14), // ежем платеж
                BigDecimal.valueOf(634.86), // %
                BigDecimal.valueOf(52129.63), // осн долг
                BigDecimal.valueOf(8354.07)));   // остаток 6 мес

        BigDecimal psk = calculatorService.calcPsk(amount, term, paymentSchedule);

        // Assert
       // BigDecimal expectedPsk = BigDecimal.valueOf(-83.000); // Ожидаемое значение PSK
        Assertions.assertEquals(BigDecimal.valueOf(10).setScale(2, RoundingMode.HALF_UP),
                psk.setScale(2, RoundingMode.HALF_UP),
                "Значение PSK некорректно");

    }

    @Test
    void testCalcPaymentScheduleReturnsCountElementsEqualsTerm(){
        List<PaymentScheduleElementDto> result;
        BigDecimal amount = BigDecimal.valueOf(300000);
        Integer term = 6;
        BigDecimal rate = BigDecimal.valueOf(15);
        BigDecimal monthlyPayment = BigDecimal.valueOf(52210.14);
        result = calculatorService.calcPaymentSchedule(amount, term, rate, monthlyPayment);

        assertEquals(term, result.size());
    }

    @Test
    // проверяем, что в графике сумма основного долга суммарно не превышает сумму кредита(с учетом остатка долга)
    void testCalcPaymentScheduleReturnsSumRemainingPaysEqualsAmount(){
        List<PaymentScheduleElementDto> result;
        PaymentScheduleElementDto pScheduleElement = new PaymentScheduleElementDto();

        BigDecimal amount = BigDecimal.valueOf(300000);
        Integer term = 6;
        BigDecimal rate = BigDecimal.valueOf(15);
        BigDecimal monthlyPayment = BigDecimal.valueOf(52210.14);

        result = calculatorService.calcPaymentSchedule(amount, term, rate, monthlyPayment);

        BigDecimal totalDebt = BigDecimal.ZERO;
        BigDecimal curAmount = amount;
        for (PaymentScheduleElementDto pElement : result) {
            totalDebt = totalDebt.add(pElement.getDebtPayment().setScale(2, RoundingMode.HALF_UP));

        }
        curAmount = curAmount.subtract(totalDebt.setScale(2, RoundingMode.HALF_UP));
        PaymentScheduleElementDto lastElement = result.get(result.size() - 1);
        lastElement.setDebtPayment(lastElement.getDebtPayment().add(curAmount).setScale(2, RoundingMode.HALF_UP));
        //totalDebt.add(curAmount.setScale(2, RoundingMode.HALF_UP));

        // повторно пересчитываем
        totalDebt = BigDecimal.ZERO;
        curAmount = amount;
        for (PaymentScheduleElementDto pElement : result) {
            totalDebt = totalDebt.add(pElement.getDebtPayment().setScale(2, RoundingMode.HALF_UP));

        }
        curAmount = curAmount.subtract(totalDebt.setScale(2, RoundingMode.HALF_UP));

        assertEquals(amount.setScale(2, RoundingMode.HALF_UP), totalDebt.setScale(2, RoundingMode.HALF_UP));
    }


    /* проверяем, что выбрасываются исключения при несоответствии условиям скоринга*/
    // запрос суммы кредита > 24 зарплат
    @Test
    void testScoringCheck24Salaries(){
        BigDecimal preRate = BigDecimal.valueOf(15);
        BigDecimal amount = BigDecimal.valueOf(300000);
        BigDecimal salary = BigDecimal.valueOf(10000);
        ScoringDataDto scoringDataDto = new ScoringDataDto();

        // Информация о занятости
        EmploymentDto employment = new EmploymentDto();
        employment.setSalary(salary);
        scoringDataDto.setEmployment(employment);

        ScoreException thrown = Assertions.assertThrows(ScoreException.class, () -> {
            calculatorService.scoringCheck(scoringDataDto);
        });
       assertEquals("Сумма займа больше 24 зарплат. Отказано.", thrown.getMessage());
    }

    // на текущем месте менее 3 месяцев
    @Test
    void testScoringCheckExperienceCurLessThan3Months() throws ScoreException {
        int experienceCur = 2;
        ScoringDataDto scoringDataDto = new ScoringDataDto();
        // Информация о занятости
        EmploymentDto employment = new EmploymentDto();
        employment.setWorkExperienceCurrent(experienceCur);
        scoringDataDto.setEmployment(employment);

        ScoreException thrown = Assertions.assertThrows(ScoreException.class, () -> {
            calculatorService.scoringCheck(scoringDataDto);
        });
        assertEquals("Стаж на текущем месте работы меньше требуемого. Отказано.", thrown.getMessage());
    }

    // общий стаж менее 18 месяцев
    @Test
    void testScoringCheckExperienceTotalLessThan18Months() throws ScoreException {
        int experienceTotal = 13;
        ScoringDataDto scoringDataDto = new ScoringDataDto();
        // Информация о занятости
        EmploymentDto employment = new EmploymentDto();
        employment.setWorkExperienceTotal(experienceTotal);
        scoringDataDto.setEmployment(employment);

        ScoreException thrown = Assertions.assertThrows(ScoreException.class, () -> {
            calculatorService.scoringCheck(scoringDataDto);
        });
        assertEquals("Общий стаж меньше требуемого. Отказано.", thrown.getMessage());
    }

    // тест на возраст
    @Test
    void testScoringCheckAgeMoreThan65(){
        ScoringDataDto scoringDataDto = new ScoringDataDto();
        scoringDataDto.setBirthdate(LocalDate.now().minusYears(66));

        // Проверяем, что метод выбрасывает исключение
        ScoreException thrown = Assertions.assertThrows(ScoreException.class, () -> {
            calculatorService.scoringCheck(scoringDataDto);
        });

        Assertions.assertEquals("Заявитель не соответствует возрастным рамкам. Отказано.", thrown.getMessage());
    }

    @Test
    void testScoringCheckAgeLessThan20(){
        ScoringDataDto scoringDataDto = new ScoringDataDto();
        scoringDataDto.setBirthdate(LocalDate.now().minusYears(19));

        // Проверяем, что метод выбрасывает исключение
        ScoreException thrown = assertThrows(ScoreException.class, () -> {
            calculatorService.scoringCheck(scoringDataDto);
        });

        assertEquals("Заявитель не соответствует возрастным рамкам. Отказано.", thrown.getMessage());
    }


    @Test
    void testGetLoanOffers() {
      //  CalculatorService calculatorService = new CalculatorService();

        LoanStatementRequestDto request = new LoanStatementRequestDto();
        request.setFirstName("Evgeniya");
        request.setLastName("Berezentseva");
        request.setMiddleName("Vladimirovna");
        request.setAmount(BigDecimal.valueOf(300000));
        request.setTerm(6);
        request.setBirthDate(LocalDate.parse("2000-12-01"));
        request.setEmail("mail.123@example.com");
        request.setPassportSeries("1255");
        request.setPassportNumber("567050");
        //  int baseRate = 15;

        // Выполнение метода
        List<LoanOfferDto> offers = calculatorService.getLoanOffers(request);

        // Проверка результатов
        assertEquals(4, offers.size(), "Должно быть 4 оффера");

        // Дополнительные проверки на содержание офферов
        assertEquals(4, offers.get(0).getRate().intValue(), "Первый оффер должен иметь наивысшую процентную ставку");
        assertEquals(1, offers.get(1).getRate().intValue(), "Второй оффер должен иметь вторую по величине процентную ставку");
    }
}