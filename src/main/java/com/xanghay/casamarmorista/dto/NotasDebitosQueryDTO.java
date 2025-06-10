package com.xanghay.casamarmorista.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface NotasDebitosQueryDTO {
    Long getId();
    Integer getClienteId();
    LocalDate getDataEmissao();
    BigDecimal getValorTotal();
    Long getIdDebito();
}
