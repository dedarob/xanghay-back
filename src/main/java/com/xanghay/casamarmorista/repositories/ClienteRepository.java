package com.xanghay.casamarmorista.repositories;

import com.xanghay.casamarmorista.models.Cliente;
import org.springframework.data.repository.CrudRepository;

public interface ClienteRepository extends CrudRepository<Cliente, Integer> {
}