package com.xanghay.casamarmorista.repositories;

import com.xanghay.casamarmorista.models.Debitos;
import org.springframework.data.repository.CrudRepository;

public interface DebitosRepository extends CrudRepository<Debitos, Long> {
    void deleteByNota_Id(Long notaId);

}
