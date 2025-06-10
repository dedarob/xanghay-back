package com.xanghay.casamarmorista.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VinculoPagamentoDebitoDTO {
    private Long idNota;
    private LocalDate dataNota;
    private Long idDebito;
    private BigDecimal valorDebito;
    private BigDecimal valorPago;
    private LocalDate dataPagamento;
    private BigDecimal valorRestanteDebito;
    private Long idPagamento;
}