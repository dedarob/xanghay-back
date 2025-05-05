package com.xanghay.casamarmorista.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name="notas")
public class Notas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate dataEmissao;

    @ManyToOne
    @JoinColumn(name="cliente_id", nullable=false)
    private Cliente cliente;
}
