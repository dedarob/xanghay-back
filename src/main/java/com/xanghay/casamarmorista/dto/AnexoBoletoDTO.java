package com.xanghay.casamarmorista.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnexoBoletoDTO {
    private Integer id;
    private byte[] anexo;
}
