package ru.afbtest.calculator.controllers;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<List<LoanOfferDto>> calculateOffers(@RequestBody LoanStatementRequestDto requestDto) {
        // Логика для расчета предложений кредита
        // на вход заявка loanstatement, вызывается сервис с прескорингом, в ответ отдаются 4 предложения
        calculatorService.preScoringCheck(requestDto);
        List<LoanOfferDto> offers = calculatorService.getLoanOffers(requestDto);
        return new ResponseEntity<>(offers, HttpStatus.OK);   // в теле ответа будут офферы
       // return List.of(); // придумать список предложений в зависимости от условий
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
