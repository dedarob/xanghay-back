package com.xanghay.casamarmorista.services;

import com.xanghay.casamarmorista.dto.NotasDetailedDTO;
import com.xanghay.casamarmorista.mappers.NotasMapper;
import com.xanghay.casamarmorista.models.Notas;
import com.xanghay.casamarmorista.repositories.NotasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotasService {

    @Autowired
    private NotasRepository notaRepo;
    @Autowired
    private NotasMapper notasMapper;

    public List<Notas> procurarNotasPeloIdDoCliente(Long clienteId){
        return notaRepo.findByCliente_Id(clienteId);
    }

    public List<NotasDetailedDTO> procurarListaDeNotasPeloIdDoCliente(Long clienteId){
        List<Notas> notasList = notaRepo.findByCliente_Id(clienteId);
        List<NotasDetailedDTO> notasDetailedDTOList = notasMapper.toNotasDetailedDTOList(notasList);
        return notasDetailedDTOList;
    }
}
