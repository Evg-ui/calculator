package ru.afbtest.calculator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.afbtest.calculator.DTO.*;
import ru.afbtest.calculator.exception.ScoreException;
import ru.afbtest.calculator.utils.PreScoring;
import ru.afbtest.calculator.utils.Scoring;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import static ru.afbtest.calculator.utils.PreScoring.amount_MIN;
import static ru.afbtest.calculator.utils.PreScoring.term_MIN;

@Slf4j
@Service        // для обозначения класса как сервиса, который содержит бизнес-логику приложения
public class CalculatorService {
    @Value("${baseRate}")
    private BigDecimal baseRate;

    // TODO: убрать прескоринг по классам своим
    /**********Это все для прескоринга***********/
    // наверно, это надо выносить в заявку или в прескоринг класс, иначе бардак вышел. Не выносится =(
    /*
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z]{2,30}$");
    private static final Pattern DATE_PATTERN = Pattern.compile("^(19\\\\d{2}|20\\\\d{2})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$");
    private static final Pattern PASSPORT_SERIES_PATTERN = Pattern.compile("^\\d{4}$");
    private static final Pattern PASSPORT_NUMBER_PATTERN = Pattern.compile("^\\d{6}$");*/

    /*метод проверки валидности имен*/
//    private boolean isValidName(String name) {
//        return NAME_PATTERN.matcher(name).matches();
//    }
//
//    /*метод проверки валидности даты рождения*/
//    private boolean isValidBirthDate(String birthDate) {
//        return DATE_PATTERN.matcher(birthDate).matches();
//    }
//
//    /*метод проверки email*/
//    private boolean isValidEmail(String email) {
//        return EMAIL_PATTERN.matcher(email).matches();
//    }
//
//    /*метод проверки валидности серии паспорта*/
//    private boolean isValidPassportSeries(String passportSeries) {
//        return PASSPORT_SERIES_PATTERN.matcher(passportSeries).matches();
//    }
//
//    /*метод проверки валидности номера паспорта*/
//    private boolean isValidPassportNumber(String passportNumber) {
//        return PASSPORT_NUMBER_PATTERN.matcher(passportNumber).matches();
//    }
    // наверно, это надо выносить в заявку или в прескоринг класс, иначе бардак вышел

        /*Процесс прескоринга*/
    public void preScoringCheck(LoanStatementRequestDto request) {
        log.info("Прескоринг...", request);

        PreScoring preScoring = new PreScoring();
        try {
            preScoring.validate(request);
            log.info("Прескоринг успешен!");
        } catch (ScoreException e) {
            log.info("Ошибка прескоринга: " + e.getMessage());
        }

//        if (!isValidName(request.getFirstName())) {
//            return "Имя должно содержать от 2 до 30 латинских букв.";
//        }
//        if (!isValidName(request.getLastName())) {
//            return "Фамилия должна содержать от 2 до 30 латинских букв.";
//        }
//        if (request.getMiddleName() != null && !isValidName(request.getMiddleName())) {
//            return "Отчество должно содержать от 2 до 30 латинских букв.";
//        }
//
//        // Проверка суммы кредита  == -1 -  значит кредит меньше 20000
//        if (request.getAmount().compareTo(amount_MIN) == -1) {
//            return "Сумма кредита должна быть больше или равна 20000.";
//        }
//
//        // Проверка срока кредита == -1 -  значит срок  меньше 6мес
//        if (request.getTerm().compareTo(term_MIN) == -1) {
//            return "Срок кредита должен быть больше или равен 6";
//        }
//
////        // Проверка даты рождения
////        if (!isValidBirthDate(request.getbirthDate())) {
////            return "Дата рождения должна быть в формате гггг-мм-дд и не позднее 18 лет с текущего дня.";
////        }
//
//        // Проверка email
//        if (!isValidEmail(request.getEmail())) {
//            return "Email адрес должен соответствовать формату.";
//        }
//
//        // Проверка серии и номера паспорта
//        if (!isValidPassportSeries(request.getPassportSeries())) {
//            return "Серия паспорта должна содержать 4 цифры.";
//        }
//        if (!isValidPassportNumber(request.getPassportNumber())) {
//            return "Номер паспорта должен содержать 6 цифр.";
//        }
//
//        return "Проверка пройдена успешно"; // Все проверки пройдены

    }


    // TODO: для предложений подобрать какие-нибудь поинтереснее условия
    /*формирование списка из 4 предложений, на вход - данные заявки, которая прошла прескоринг */
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto requestDto){
        List<LoanOfferDto> offersDto = new ArrayList<>();
        BigDecimal preMonthlyPayment, preTotalAmount, preRate;  // для предложений

        /*поочередно им рассчитываем показатели*/
        preRate = baseRate.add(BigDecimal.valueOf(2));    // повышаем базовую ставку, если нет зп и нет страховки на 2%
        preMonthlyPayment = this.calcMonthlyPayment(requestDto.getAmount(), baseRate, requestDto.getTerm());
        preTotalAmount = preMonthlyPayment.multiply(BigDecimal.valueOf(requestDto.getTerm()));
        LoanOfferDto current1 = LoanOfferDto.builder()
                .isSalaryClient(false)
                .isInsuranceEnabled(false)
                .term(requestDto.getTerm())
                .requestedAmount(requestDto.getAmount())
                .rate(preRate)     // сохраняем базовую ставку, если нет зп и нет страховки
                .term(requestDto.getTerm())
                .totalAmount(preTotalAmount)
                .monthlyPayment(preMonthlyPayment)
                .statementId(UUID.randomUUID()) // Генерация случайного UUID
                .build();
        offersDto.add(current1);

        preRate = baseRate.subtract(BigDecimal.valueOf(1));   // уменьшаем ставку на 1% клиенту с зп
        preMonthlyPayment = this.calcMonthlyPayment(requestDto.getAmount(), preRate, requestDto.getTerm());
        preTotalAmount = preMonthlyPayment.multiply(BigDecimal.valueOf(requestDto.getTerm()));
        LoanOfferDto current2 = LoanOfferDto.builder()
                .isSalaryClient(true)
                .isInsuranceEnabled(false)
                .term(requestDto.getTerm())
                .requestedAmount(requestDto.getAmount())
                .rate(preRate)
                .term(requestDto.getTerm())
                .totalAmount(preTotalAmount)
                .monthlyPayment(preMonthlyPayment)
                .statementId(UUID.randomUUID())
                .build();
        offersDto.add(current2);

        preRate = baseRate.add(BigDecimal.valueOf(1));   // повышаем ставку на 1% клиенту со страховкой и без зп
        preMonthlyPayment = this.calcMonthlyPayment(requestDto.getAmount(), preRate, requestDto.getTerm());
        preTotalAmount = preMonthlyPayment.multiply(BigDecimal.valueOf(requestDto.getTerm()));
        LoanOfferDto current3 = LoanOfferDto.builder()
                .isSalaryClient(false)
                .isInsuranceEnabled(true)
                .term(requestDto.getTerm())
                .requestedAmount(requestDto.getAmount())
                .rate(preRate)
                .term(requestDto.getTerm())
                .totalAmount(preTotalAmount)
                .monthlyPayment(preMonthlyPayment)
                .statementId(UUID.randomUUID())
                .build();
        offersDto.add(current3);

        preRate = baseRate.subtract(BigDecimal.valueOf(3));   // уменьшаем ставку на 3% клиенту со страховкой и c зп
        preMonthlyPayment = this.calcMonthlyPayment(requestDto.getAmount(), preRate, requestDto.getTerm());
        preTotalAmount = preMonthlyPayment.multiply(BigDecimal.valueOf(requestDto.getTerm()));
        LoanOfferDto current4 = LoanOfferDto.builder()
                .isSalaryClient(true)
                .isInsuranceEnabled(true)
                .term(requestDto.getTerm())
                .requestedAmount(requestDto.getAmount())
                .rate(preRate)
                .term(requestDto.getTerm())
                .totalAmount(preTotalAmount)
                .monthlyPayment(preMonthlyPayment)
                .statementId(UUID.randomUUID())
                .build();
        offersDto.add(current4);

      //  offersDto.sort(Comparator.(offersDto::get));

        return offersDto;
    }

    /*расчет ежемесячного платежа от суммы кредита, ставки и срока*/
    public BigDecimal calcMonthlyPayment(BigDecimal amount,
                                         BigDecimal rate,
                                         Integer term) {
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(1200), MathContext.DECIMAL64); // Преобразование годовой ставки в месячную
        return monthlyRate.add(BigDecimal.ONE)
                .pow(term)
                .multiply(monthlyRate)
                .divide(monthlyRate.add(BigDecimal.ONE).pow(term).subtract(BigDecimal.ONE), MathContext.DECIMAL64)
                .multiply(amount);
    }

    public List<PaymentScheduleElementDto> calcPaymentSchedule(BigDecimal amount,
                                                               Integer term,
                                                               BigDecimal rate,
                                                               BigDecimal monthlyPayment)
{
    BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(1200), MathContext.DECIMAL64); // Преобразование годовой ставки в месячную
    List<PaymentScheduleElementDto> result = new ArrayList<>();
    PaymentScheduleElementDto pScheduleElement = new PaymentScheduleElementDto();
    LocalDate payDate = LocalDate.now();
    BigDecimal curAmount = amount; // для расчета остатка надо запоминать текущие суммы

    for(int i =0; i < term; i++){
        pScheduleElement = new PaymentScheduleElementDto();
        pScheduleElement.setNumber(i+1);
        pScheduleElement.setDate(payDate.plusMonths(i+1));
        pScheduleElement.setTotalPayment(monthlyPayment.setScale(2, RoundingMode.HALF_UP));
        pScheduleElement.setInterestPayment(curAmount.multiply(monthlyRate).setScale(2,RoundingMode.HALF_UP));
        pScheduleElement.setDebtPayment(monthlyPayment.subtract(pScheduleElement.getInterestPayment()).setScale(2,RoundingMode.HALF_UP));
        pScheduleElement.setRemainingDebt(curAmount.negate().subtract(pScheduleElement.getDebtPayment().setScale(2, RoundingMode.HALF_UP)));

        /*уменьшаем основной долг на сумму уплаченного по графику*/
        curAmount = curAmount.subtract(pScheduleElement.getDebtPayment());

        result.add(pScheduleElement);
        }

    // Проверяем остаток долга

    if (curAmount.compareTo(BigDecimal.ZERO) != 0) {
        log.info("График рассчитан некорректно. Остаток долга: %s"
                .formatted(curAmount.setScale(2, RoundingMode.HALF_UP).toString()));

        // Добавляем остаток к последнему элементу графика
        PaymentScheduleElementDto lastElement = result.get(result.size() - 1);
        lastElement.setTotalPayment(lastElement.getTotalPayment().add(curAmount).setScale(2, RoundingMode.HALF_UP));
        lastElement.setRemainingDebt(lastElement.getRemainingDebt().add(curAmount.negate()).setScale(2, RoundingMode.HALF_UP));
    }

    return result;

    }

    public BigDecimal scoringCheck(ScoringDataDto scoringDataDto) throws ScoreException {
        BigDecimal preRate = baseRate;

        /*Сумма займа больше, чем 24 зарплаты --> Отказ*/
        if (scoringDataDto.getEmployment().getSalary().multiply(BigDecimal.valueOf(24)).compareTo(scoringDataDto.getAmount()) != 1)
        {
            throw new ScoreException("Сумма займа больше 24 зарплат. Отказано.");
        }
        /*Общий стаж менее 18 месяцев → Отказ*/
        if (scoringDataDto.getEmployment().getWorkExperienceTotal() < 18)
        {
            throw new ScoreException("Общий стаж меньше требуемого. Отказано.");
        }
        /*Текущий стаж менее 3 месяцев → Отказ*/
        if (scoringDataDto.getEmployment().getWorkExperienceCurrent() < 3)
        {
            throw new ScoreException("Стаж на текущем месте работы меньше требуемого. Отказано.");
        }
        /*Возраст менее 20 или более 65 лет → отказ*/
        int age = Period.between(scoringDataDto.getBirthdate(), LocalDate.now()).getYears();
        if (age < 20 || age > 65)
            throw new ScoreException("Заявитель не соответствует возрастным рамкам. Отказано.");

        /*Рабочий статус: Безработный → отказ; Самозанятый → ставка увеличивается на 2; Владелец бизнеса → ставка увеличивается на 1*/
        preRate = preRate.add(Scoring.getEmploymentRate(scoringDataDto.getEmployment().getEmploymentStatus()));

        // TODO: опять метод getPositionRate куда-то делся
        /*Позиция на работе: Менеджер среднего звена → ставка уменьшается на 2; Топ-менеджер → ставка уменьшается на */
        //preRate = preRate.add(Scoring.getPositionRate(scoringDataDto.getEmployment().getPosition()));

        /*Семейное положение: Замужем/женат → ставка уменьшается на 3; Разведен → ставка увеличивается на 1*/
        preRate = preRate.add(Scoring.getMaritalStatusRate(scoringDataDto.getMaritalStatus()));

        /*Пол: Женщина, возраст от 32 до 60 лет → ставка уменьшается на 3;*/
        /*Мужчина, возраст от 30 до 55 лет → ставка уменьшается на 3; Не бинарный → ставка увеличивается на 7*/
        preRate = preRate.add(Scoring.getGenderAndAgeRate(scoringDataDto.getGender(), age));
        return preRate;
    }

    BigDecimal calcPsk(BigDecimal amount, Integer term,
                       List<PaymentScheduleElementDto> paymentSchedule){
        log.info("Расчет ПСК...");

       /*Суммируем все платежи из графика*/
        BigDecimal totalPay = BigDecimal.ZERO;
        for (int i = 0; i < paymentSchedule.size(); i++) {
            totalPay = totalPay.add(paymentSchedule.get(i).getTotalPayment());
                    }
        System.out.println("Сумма всех платежей = " + totalPay);

        /*месяцы в годы*/
        BigDecimal termY = BigDecimal.valueOf(term).divide(BigDecimal.valueOf(12), 2, RoundingMode.CEILING);

        /*ПСК*/
        BigDecimal psk = totalPay.divide(amount, 2, RoundingMode.CEILING)
                .subtract(BigDecimal.ONE).divide(termY, 3, RoundingMode.CEILING)
                .multiply(BigDecimal.valueOf(100));

        log.info("Расчет ПСК завершен!");
        return amount;
    }

    public CreditDto calcCredit (ScoringDataDto scoringDataDto) throws ScoreException {
        CreditDto creditDto = new CreditDto();
        BigDecimal rate = scoringCheck(scoringDataDto);
        creditDto.setAmount(scoringDataDto.getAmount());
        creditDto.setTerm(scoringDataDto.getTerm());
        creditDto.setMonthlyPayment(calcMonthlyPayment(scoringDataDto.getAmount(), rate, scoringDataDto.getTerm()));
        creditDto.setRate(rate);
        creditDto.setPsk(calcPsk(scoringDataDto.getAmount(), scoringDataDto.getTerm(),  creditDto.getPaymentSchedule()));
        creditDto.setIsInsuranceEnabled(scoringDataDto.getIsInsuranceEnabled());
        creditDto.setIsSalaryClient(scoringDataDto.getIsSalaryClient());
        creditDto.setPaymentSchedule(calcPaymentSchedule(scoringDataDto.getAmount(),scoringDataDto.getTerm(),
                rate, creditDto.getMonthlyPayment()));

        System.out.println(creditDto);
        return creditDto;
    }
    }
