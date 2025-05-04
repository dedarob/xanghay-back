package com.xanghay.casamarmorista.services;

import com.xanghay.casamarmorista.dto.ClienteListDTO;
import com.xanghay.casamarmorista.mappers.ClienteMapper;
import com.xanghay.casamarmorista.models.Cliente;
import com.xanghay.casamarmorista.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteMapper mapper;
    @Autowired
    private ClienteRepository repo;

    //esse daqui retorna somente id e username
    public List<ClienteListDTO> getIdAndUsername(){
        List<Cliente> clienteList = (List<Cliente>) repo.findAll();
        return mapper.toDtoList(clienteList);
    }

    //esse daqui retorna todos os dados do model
    public List<Cliente>  getAllUsers() {
        List<Cliente> cliente = (List<Cliente>) repo.findAll();
        System.out.println(cliente);
        return cliente;
    }
}
