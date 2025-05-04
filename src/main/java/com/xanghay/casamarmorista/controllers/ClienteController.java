package com.xanghay.casamarmorista.controllers;

import com.xanghay.casamarmorista.dto.ClienteListDTO;
import com.xanghay.casamarmorista.models.Cliente;
import com.xanghay.casamarmorista.repositories.ClienteRepository;
import com.xanghay.casamarmorista.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/cliente")
public class ClienteController {
    @Autowired
    private ClienteRepository repo;
    @Autowired
    private ClienteService service;

    //Ajustar o get para receber request param de "id-username", terei que fazer uma service layer
    @GetMapping
    public ResponseEntity<?> listCliente(@RequestParam(value = "returnTypes", required = false) String returnTypes){
        if ("idAndNome".equals(returnTypes)){
            List<ClienteListDTO> clienteIdENome= service.getIdAndUsername();
            return ResponseEntity.ok().body(clienteIdENome);
        } else {
            List<Cliente> cliente = service.getAllUsers();
            return ResponseEntity.ok().body(cliente);
        }

    }

    @PostMapping
    public ResponseEntity<Cliente> createCliente(@RequestBody Cliente cliente){
        Cliente newCliente =repo.save(cliente);
        return ResponseEntity.status(201).body(newCliente);
    }


}
