package ru.berezentseva.calculator.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.berezentseva.calculator.CalculatorService;
import ru.berezentseva.calculator.DTO.CreditDto;
import ru.berezentseva.calculator.DTO.LoanOfferDto;
import ru.berezentseva.calculator.DTO.LoanStatementRequestDto;
import ru.berezentseva.calculator.DTO.ScoringDataDto;
import ru.berezentseva.calculator.exception.ScoreException;

import java.util.List;

@Slf4j
@Tag(name = "Контроллер кредитного калькулятора",
        description = "Принимается заявка от потенциального заемщика для предварительного и полного расчета кредита")
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
            summary = "Расчёт возможных условий кредита на основании заявки. ",
            description = "На вход подается заявка на кредит от потенциального заемщика." +
                    "На выходе получаем 4 предложения, отсортированных от худшего (наивысшая ставка среди предложений) " +
                    "к лучшему (наименьшая ставка среди предложений)."
    )

    // расчёт возможных условий кредита. Request - LoanStatementRequestDto, response - List<LoanOfferDto>
    @PostMapping("/offers")

    public ResponseEntity<List<LoanOfferDto>> calculateOffers(@RequestBody LoanStatementRequestDto requestDto) {
        log.info("Метод  /calculator/offers. Запрос: {}", requestDto.toString());
        calculatorService.preScoringCheck(requestDto);
        List<LoanOfferDto> offers = calculatorService.getLoanOffers(requestDto);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    // для документирования REST API, созданных с использованием Spring
    @Operation(
            summary = "На вход подаются данные из предварительной заявки на кредит. ",
            description = "Ожидается полный расчет всех параметров кредита " +
                            "на основании полных данных о заемщике " +
                            "с графиком платежей, ПСК, ежемесячным платежом."
    )
    // валидация присланных данных + скоринг данных + полный расчет параметров кредита.
    // Request - ScoringDataDto, response - CreditDto.
    @PostMapping("/calc")
    public ResponseEntity<CreditDto> calculateCredit(@RequestBody ScoringDataDto scoringData) throws ScoreException {
        log.info("Метод  /calculator/calc Запрос: {}", scoringData.toString());
        try {
        CreditDto creditDto = new CreditDto();
        calculatorService.calcCredit(scoringData);
       return new ResponseEntity<>(creditDto, HttpStatus.OK);
        } catch (ScoreException e) {
            log.info(e.getMessage());
            return ResponseEntity.unprocessableEntity().header("error", e.getMessage()).build();
        }
    }


}
