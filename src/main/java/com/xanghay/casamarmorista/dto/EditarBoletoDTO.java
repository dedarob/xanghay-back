package com.xanghay.casamarmorista.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditarBoletoDTO {
    private String descricao;
    private LocalDate dataVencimento;
    private String situacao;
    private BigDecimal valor;
    private String observacoes;
    private LocalDate dataPagamento;
    private String banco;
}

