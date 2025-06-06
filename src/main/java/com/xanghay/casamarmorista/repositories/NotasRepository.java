package com.xanghay.casamarmorista.repositories;

import com.xanghay.casamarmorista.models.Notas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotasRepository extends JpaRepository<Notas, Integer> {
    List<Notas> findByCliente_Id(Long clienteId);
    List<Notas> findAllByCliente_Id(Long clienteId);
}