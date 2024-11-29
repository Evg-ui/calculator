package ru.afbtest.calculator.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.afbtest.calculator.CalculatorService;
import ru.afbtest.calculator.DTO.CreditDto;
import ru.afbtest.calculator.DTO.LoanOfferDto;
import ru.afbtest.calculator.DTO.LoanStatementRequestDto;
import ru.afbtest.calculator.DTO.ScoringDataDto;
import ru.afbtest.calculator.exception.ScoreException;

import java.util.List;

@Tag(name = "Контроллер кредитного калькулятора",
        description = "Принимается заявка от пользователя для предварительного и полного расчета кредита")
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
            summary = "Расчёт возможных условий кредита.",
            description = "На вход подается заявка на кредит."     )

    // расчёт возможных условий кредита. Request - LoanStatementRequestDto, response - List<LoanOfferDto>
    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDto>> calculateOffers(@RequestBody LoanStatementRequestDto requestDto) {
        calculatorService.preScoringCheck(requestDto);
        List<LoanOfferDto> offers = calculatorService.getLoanOffers(requestDto);
        return new ResponseEntity<>(offers, HttpStatus.OK);   // в теле ответа будут офферы
    }


        // TODO исправить под calc
    // для документирования REST API, созданных с использованием Spring
    @Operation(
            summary = "На вход подается предварительная заявка на кредит",
            description = "Ожидается" // написать что-то надо
    )
    // валидация присланных данных + скоринг данных + полный расчет параметров кредита.
    // Request - ScoringDataDto, response CreditDto.
    @PostMapping("/calc")
    public CreditDto calculateCredit(@RequestBody ScoringDataDto scoringData) throws ScoreException {
        // Логика для валидации, скоринга и расчета кредита
        // на выход 4 предложения: взывается сервис с расчетом
        CreditDto creditDto = new CreditDto();
        calculatorService.calcCredit(scoringData);


        return creditDto;
    }


}
