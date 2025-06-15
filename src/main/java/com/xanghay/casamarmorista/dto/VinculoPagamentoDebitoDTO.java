package com.xanghay.casamarmorista.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VinculoPagamentoDebitoDTO {
    private Long idNota;
    private LocalDate dataNota;
    private Long idDebito;
    private BigDecimal valorDebito;
    private BigDecimal valorPago;
    private LocalDate dataPagamento;
    private BigDecimal valorRestanteDebito;
    private Long idPagamento;
    private BigDecimal valorAtualDebitoPosPag;
}