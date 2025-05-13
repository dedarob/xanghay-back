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
public class NotasDetailedDTO {
    private Integer id;
    private Integer idCliente;
    private String nomeCliente;
    private LocalDate dataEmissao;
    private BigDecimal valorTotal;
}
