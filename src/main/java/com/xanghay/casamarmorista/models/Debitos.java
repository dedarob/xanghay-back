package com.xanghay.casamarmorista.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "debitos")
public class Debitos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private BigDecimal valorTotal;

    @OneToOne
    @JoinColumn(name = "nota_id", nullable = false)
    private Notas nota;

}
