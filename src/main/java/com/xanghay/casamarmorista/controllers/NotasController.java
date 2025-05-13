package com.xanghay.casamarmorista.controllers;

import com.xanghay.casamarmorista.dto.NotasDetailedDTO;
import com.xanghay.casamarmorista.models.Notas;
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

    @GetMapping("/{id}")
    public ResponseEntity<List<Notas>> listNotasPorId(@PathVariable Integer id){
            List<Notas> notasByCliente= service.procurarNotasPeloIdDoCliente(id.longValue());
            return ResponseEntity.ok().body(notasByCliente);
    }

    @GetMapping("/detailed/{id}")
    public ResponseEntity<List<NotasDetailedDTO>> listNotasDetalhadasPorId(@PathVariable Integer id){
        List<NotasDetailedDTO> listNotasCliente = service.procurarListaDeNotasPeloIdDoCliente(id.longValue());
        return ResponseEntity.ok().body(listNotasCliente);
    }
}
