package ru.afbtest.calculator.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.afbtest.calculator.CalculatorService;
import ru.afbtest.calculator.DTO.CreditDto;
import ru.afbtest.calculator.DTO.LoanOfferDto;
import ru.afbtest.calculator.DTO.LoanStatementRequestDto;
import ru.afbtest.calculator.DTO.ScoringDataDto;

import java.util.List;

@RestController
@RequestMapping("/calculator")
public class CalculatorController {

    private final CalculatorService calculatorService;

    @Autowired     // автоматическое внедрение зависимостей в компоненты приложения
    public CalculatorController(CalculatorService calculatorService)
    {
        this.calculatorService = calculatorService;
    }

    // для документирования REST API, созданных с использованием Spring
    @Operation(
            summary = "На вход подается предварительная заявка на кредит",
            description = "Ожидается" // написать что-то надо
    )

    // расчёт возможных условий кредита. Request - LoanStatementRequestDto, response - List<LoanOfferDto>
    @PostMapping("/offers")
    public List<LoanOfferDto> calculateOffers(@RequestBody LoanStatementRequestDto request) {
        // Логика для расчета предложений кредита
        // на вход заявка loanstatement, вызвыается сервис с прескорингом
        return List.of(); // придумать список предложений
    }

    // валидация присланных данных + скоринг данных + полный расчет параметров кредита.
    // Request - ScoringDataDto, response CreditDto.
    @PostMapping("/calc")
    public CreditDto calculateCredit(@RequestBody ScoringDataDto scoringData) {
        // Логика для валидации, скоринга и расчета кредита
        // на выход 4 предложения: взывается сервис с расчетом
        CreditDto creditDto = new CreditDto();
        return creditDto;
    }


}
