package com.xanghay.casamarmorista.repositories;

import com.xanghay.casamarmorista.models.Pagamentos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagamentosRepository extends JpaRepository<Pagamentos, Long> {
    List<Pagamentos> findAllByCliente_Id(Long clienteId);
}
