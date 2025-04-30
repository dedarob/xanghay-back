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
    private ClienteService service;

    //Ajustar o get para receber request param de "id-username", terei que fazer uma service layer
    @GetMapping
    public ResponseEntity<?> listCliente(@RequestParam("returnTypes") String returnTypes, @RequestParam(value="name", required = false) String name){
        if ("idAndNome".equals(returnTypes)){
            List<ClienteListDTO> clienteIdeNome= service.getIdAndUsername();
            return ResponseEntity.ok().body(clienteIdeNome);
        }
        List<Cliente> list = (List<Cliente>) repo.findAll();
        System.out.println("TESTE GET SENDO EXECUTADO");
        System.out.println(list);
        return ResponseEntity.status(200).body(list);

    }

    @PostMapping
    public ResponseEntity<Cliente> createCliente(@RequestBody Cliente cliente){
        Cliente newCliente =repo.save(cliente);
        return ResponseEntity.status(201).body(newCliente);
    }


}
