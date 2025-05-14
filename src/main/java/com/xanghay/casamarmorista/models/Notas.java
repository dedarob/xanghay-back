package com.xanghay.casamarmorista.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "notas")
public class Notas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate dataEmissao;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnore
    private Cliente cliente;

    @JsonProperty("clienteId")
    public Integer getClienteId() {
        return cliente != null ? cliente.getId() : null;
    }
    @OneToOne(mappedBy = "nota")
    private Debitos debitos;

    @OneToMany(mappedBy = "nota")
    @JsonBackReference
    private Set<Itens> itens;
}
