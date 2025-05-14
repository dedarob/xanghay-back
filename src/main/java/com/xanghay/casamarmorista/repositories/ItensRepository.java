package com.xanghay.casamarmorista.repositories;

import com.xanghay.casamarmorista.models.Itens;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItensRepository extends CrudRepository<Itens, Long> {
    List<Itens> findByNota_Id(Long notaId);
}