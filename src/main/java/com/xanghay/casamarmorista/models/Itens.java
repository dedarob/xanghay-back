package com.xanghay.casamarmorista.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="itens_nota")
public class Itens {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notaId;
}
