package ru.afbtest.calculator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.afbtest.calculator.DTO.LoanOfferDto;
import ru.afbtest.calculator.DTO.LoanStatementRequestDto;
import ru.afbtest.calculator.DTO.ScoringDataDto;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import static ru.afbtest.calculator.utils.PreScoring.amount_MIN;
import static ru.afbtest.calculator.utils.PreScoring.term_MIN;

@Service        // для обозначения класса как сервиса, который содержит бизнес-логику приложения
public class CalculatorService {
    @Value("${baseRate}")
    private BigDecimal baseRate;

    // TODO: убрать прескоринг по классам своим
    /**********Это все для прескоринга***********/
    // наверно, это надо выносить в заявку или в прескоринг класс, иначе бардак вышел. Не выносится =(
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z]{2,30}$");
    private static final Pattern DATE_PATTERN = Pattern.compile("^(19\\\\d{2}|20\\\\d{2})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$");
    private static final Pattern PASSPORT_SERIES_PATTERN = Pattern.compile("^\\d{4}$");
    private static final Pattern PASSPORT_NUMBER_PATTERN = Pattern.compile("^\\d{6}$");

    /*метод проверки валидности имен*/
    private boolean isValidName(String name) {
        return NAME_PATTERN.matcher(name).matches();
    }

    /*метод проверки валидности даты рождения*/
    private boolean isValidBirthDate(String birthDate) {
        return DATE_PATTERN.matcher(birthDate).matches();
    }

    /*метод проверки email*/
    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /*метод проверки валидности серии паспорта*/
    private boolean isValidPassportSeries(String passportSeries) {
        return PASSPORT_SERIES_PATTERN.matcher(passportSeries).matches();
    }

    /*метод проверки валидности номера паспорта*/
    private boolean isValidPassportNumber(String passportNumber) {
        return PASSPORT_NUMBER_PATTERN.matcher(passportNumber).matches();
    }
    // наверно, это надо выносить в заявку или в прескоринг класс, иначе бардак вышел

        /*Процесс прескоринга*/
    public String preScoringCheck(LoanStatementRequestDto request){

        if (!isValidName(request.getFirstName())) {
            return "Имя должно содержать от 2 до 30 латинских букв.";
        }
        if (!isValidName(request.getLastName())) {
            return "Фамилия должна содержать от 2 до 30 латинских букв.";
        }
        if (request.getMiddleName() != null && !isValidName(request.getMiddleName())) {
            return "Отчество должно содержать от 2 до 30 латинских букв.";
        }

        // Проверка суммы кредита  == -1 -  значит кредит меньше 20000
        if (request.getAmount().compareTo(amount_MIN) == -1) {
            return "Сумма кредита должна быть больше или равна 20000.";
        }

        // Проверка срока кредита == -1 -  значит срок  меньше 6мес
        if (request.getTerm().compareTo(term_MIN) == -1) {
            return "Срок кредита должен быть больше или равен 6 месяцев.";
        }

//        // Проверка даты рождения
//        if (!isValidBirthDate(request.getbirthDate())) {
//            return "Дата рождения должна быть в формате гггг-мм-дд и не позднее 18 лет с текущего дня.";
//        }

        // Проверка email
        if (!isValidEmail(request.getEmail())) {
            return "Email адрес должен соответствовать формату.";
        }

        // Проверка серии и номера паспорта
        if (!isValidPassportSeries(request.getPassportSeries())) {
            return "Серия паспорта должна содержать 4 цифры.";
        }
        if (!isValidPassportNumber(request.getPassportNumber())) {
            return "Номер паспорта должен содержать 6 цифр.";
        }

        return "Проверка пройдена успешно"; // Все проверки пройдены

    }

    /*формирование списка из 4 предложений, на вход - данные заявки, которая прошла прескоринг */
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto requestDto){
        List<LoanOfferDto> offersDto = new ArrayList<>();
        BigDecimal preMonthlyPayment, preTotalAmount, preRate;  // для предложений

        /*поочередно им рассчитываем показатели*/
        BigDecimal baseRate = BigDecimal.valueOf(15);  // это убрать, должно из пропертис браться
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

        return offersDto;
    }

    /*расчет ежемесячного платежа от суммы кредита, ставки и срока*/
    public BigDecimal calcMonthlyPayment(BigDecimal amount, BigDecimal rate, Integer term) {
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(1200), MathContext.DECIMAL64); // Преобразование годовой ставки в месячную
        return monthlyRate.add(BigDecimal.ONE)
                .pow(term)
                .multiply(monthlyRate)
                .divide(monthlyRate.add(BigDecimal.ONE).pow(term).subtract(BigDecimal.ONE), MathContext.DECIMAL64)
                .multiply(amount);
    }

    // TODO: метод написать
    public String ScoringCheck(ScoringDataDto request){
        return "";
    }


}
