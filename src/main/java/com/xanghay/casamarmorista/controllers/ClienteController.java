package com.xanghay.casamarmorista.controllers;

import com.xanghay.casamarmorista.models.Cliente;
import com.xanghay.casamarmorista.repositories.ClienteRepository;
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

    @GetMapping
    public ResponseEntity<List<Cliente>> getCliente(){
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
