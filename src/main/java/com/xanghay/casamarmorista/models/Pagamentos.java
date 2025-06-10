package com.xanghay.casamarmorista.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name="pagamentos")
public class Pagamentos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonBackReference
    private Cliente cliente;
    private LocalDate dataPagamento;
    private BigDecimal valorPago;

    @JsonProperty("clienteId")
    public Integer getClienteId() {
        return cliente != null ? cliente.getId() : null;
    }

    @Override
    public String toString() {
        return "Pagamentos{" +
                "id=" + id +
                ", clienteId=" + (cliente != null ? cliente.getId() : null) +
                ", dataPagamento=" + dataPagamento +
                ", valorPago=" + valorPago +
                '}';
    }
}
