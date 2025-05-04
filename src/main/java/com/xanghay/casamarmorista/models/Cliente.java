package com.xanghay.casamarmorista.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nomeCliente;
    private String cidadeCliente;
    private String enderecoCliente;
    private String telefoneCliente;
    @OneToMany(mappedBy="cliente")
    private Set<Notas> notas;
}
