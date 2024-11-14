package ru.afbtest.calculator.DTO;

import ru.afbtest.calculator.DTO.Enums.ChangeType;
import ru.afbtest.calculator.DTO.Enums.Status;

import java.time.LocalDateTime;

public class StatementStatusHistoryDto {
    private Status status;
    private LocalDateTime time;
    private ChangeType changeType;


}
