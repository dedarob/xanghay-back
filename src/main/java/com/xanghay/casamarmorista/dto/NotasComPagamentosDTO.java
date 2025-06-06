package com.xanghay.casamarmorista.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotasComPagamentosDTO {
    private LocalDate dataNota;
    private List<LocalDate> dataPagamentos = new ArrayList<>();
    private BigDecimal valorPagoOriginal;
    private BigDecimal sobra;
    private Long idNota;
    private List<Long> idPagamentos = new ArrayList<>();
    private String situacao;
}
