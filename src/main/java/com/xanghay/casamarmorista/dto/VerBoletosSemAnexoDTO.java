package com.xanghay.casamarmorista.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Lob;
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
public class                        VerBoletosSemAnexoDTO {
    private Integer id;
    private String descricao;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataVencimento;
    private String situacao;
    private BigDecimal valor;
    private String observacoes;
    private LocalDate dataPagamento;
    private String banco;
}

