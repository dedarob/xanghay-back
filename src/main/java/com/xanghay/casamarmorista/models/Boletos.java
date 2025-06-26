package com.xanghay.casamarmorista.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name="boletos")
public class Boletos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String descricao;
    private LocalDate dataVencimento;
    private String situacao;
    @Lob
    private byte[] anexo;
    private BigDecimal valor;
    private String observacoes;
    private LocalDate dataPagamento;
    private String banco;
}
