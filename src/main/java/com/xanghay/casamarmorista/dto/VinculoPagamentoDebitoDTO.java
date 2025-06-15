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
    private BigDecimal valorDebito;
    private Long idPagamento;
    private BigDecimal valorPagoOriginal;
    private Long idDebito;
    private LocalDate dataNota;
    private LocalDate dataPagamento;
    private BigDecimal valorAtualDebitoPosPagAnterior;
    private BigDecimal valorPago;
    private BigDecimal valorRestanteDebito;
    private String situacao;
    private Boolean isSobraSomente;
}