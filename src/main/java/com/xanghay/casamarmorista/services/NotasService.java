package com.xanghay.casamarmorista.services;

import com.xanghay.casamarmorista.models.Notas;
import com.xanghay.casamarmorista.repositories.NotasRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class NotasService {

    @Autowired
    private NotasRepository notaRepo;

    public List<Notas> procurarNotasPeloIdDoCliente(Long clienteId){
        return notaRepo.findByClienteId(clienteId);
    }
}
