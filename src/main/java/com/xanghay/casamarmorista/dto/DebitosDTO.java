    package com.xanghay.casamarmorista.dto;

    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.math.BigDecimal;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public class DebitosDTO {
        private BigDecimal valorTotal;
    }
