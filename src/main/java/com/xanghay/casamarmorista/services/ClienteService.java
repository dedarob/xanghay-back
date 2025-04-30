package com.xanghay.casamarmorista.services;

import com.xanghay.casamarmorista.dto.ClienteListDTO;
import com.xanghay.casamarmorista.mappers.ClienteMapper;
import com.xanghay.casamarmorista.models.Cliente;
import com.xanghay.casamarmorista.repositories.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private ClienteListDTO repoList;
    private ClienteMapper mapper;
    private ClienteRepository repo;

    //esse daqui retorna somente id e username
    public List<ClienteListDTO> getIdAndUsername(){
        List<Cliente> clienteList = (List<Cliente>) repo.findAll();
        return mapper.toDtoList(clienteList);
    }

    //esse daqui retorna todos os dados do model
    public List<Cliente>  getAllUsers() {
        return (List<Cliente>) repo.findAll();
    }
}
