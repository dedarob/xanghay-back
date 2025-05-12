package com.xanghay.casamarmorista.controllers;

import com.xanghay.casamarmorista.dto.ClienteListDTO;
import com.xanghay.casamarmorista.models.Cliente;
import com.xanghay.casamarmorista.services.NotasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/notas")
public class NotasController {
    @Autowired
    private NotasService service;

    @GetMapping
    public ResponseEntity<?> listNotas(@RequestParam(value = "returnTypes", required = false) String returnTypes){
        if ("byClienteId".equals(returnTypes)){
            List<ClienteListDTO> notasByCliente= service.procurarNotasPeloIdDoCliente();
            return ResponseEntity.ok().body(clienteIdENome);
        } else {
            List<Cliente> cliente = service.getAllUsers();
            return ResponseEntity.ok().body(cliente);
        }

    }
}
