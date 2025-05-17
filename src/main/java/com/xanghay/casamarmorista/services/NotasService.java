package com.xanghay.casamarmorista.services;

import com.xanghay.casamarmorista.dto.CriarNotaDTO;
import com.xanghay.casamarmorista.dto.NotasDetailedDTO;
import com.xanghay.casamarmorista.mappers.NotasMapper;
import com.xanghay.casamarmorista.models.Cliente;
import com.xanghay.casamarmorista.models.Debitos;
import com.xanghay.casamarmorista.models.Itens;
import com.xanghay.casamarmorista.models.Notas;
import com.xanghay.casamarmorista.repositories.ItensRepository;
import com.xanghay.casamarmorista.repositories.NotasRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotasService {

    @Autowired
    private NotasRepository notaRepo;
    @Autowired
    private NotasMapper notasMapper;
    @Autowired
    private ItensRepository itensRepository;
    @Autowired
    private ClienteService clienteService;

    public List<Notas> procurarNotasPeloIdDoCliente(Long clienteId){
        return notaRepo.findByCliente_Id(clienteId);
    }

    public List<NotasDetailedDTO> procurarListaDeNotasPeloIdDoCliente(Long clienteId){
        List<Notas> notasList = notaRepo.findByCliente_Id(clienteId);
        List<NotasDetailedDTO> notasDetailedDTOList = notasMapper.toNotasDetailedDTOList(notasList);
        return notasDetailedDTOList;
    }

    public List<Itens> procurarItensPeloIdNota(Long notaId){
        List<Itens> itensList = itensRepository.findByNota_Id(notaId);
        return itensList;
    }
    @Transactional
    public Notas registrarNotasComItens(CriarNotaDTO dto){
            Notas nota = notasMapper.toEntity(dto.getNotaSimples());
            Cliente cliente = clienteService.findById(dto.getNotaSimples().getClienteId());
            nota.setCliente(cliente);

            List<Itens> itens = dto.getItens().stream()
                    .map(notasMapper::toEntity)
                    .peek(item -> item.setNota(nota))
                    .toList();
            nota.setItens(itens.stream().collect(Collectors.toSet()));
            Debitos debito = notasMapper.toEntity(dto.getDebitos());
            debito.setNota(nota);
            nota.setDebitos(debito);
            return notaRepo.save(nota);
        }

    }

