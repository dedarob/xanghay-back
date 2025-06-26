package com.xanghay.casamarmorista.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
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
public class EnviarBoletosDTO {
    private String descricao;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataVencimento;
    private String situacao;
    @Lob
    private byte[] anexo;
    private BigDecimal valor;
    private String observacoes;
    private LocalDate dataPagamento;
    private String banco;
}
