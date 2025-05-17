package com.xanghay.casamarmorista.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name="itens_nota")
public class Itens {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String descricao;
    private String quantidade;
    private BigDecimal precoUnitario;

    @ManyToOne
    @JoinColumn(name = "nota_id", nullable = false)
    @JsonBackReference
    private Notas nota;

    @JsonProperty("notaId")
    public Integer getNotaId() {
        return nota != null ? nota.getId() : null;
    }
}
