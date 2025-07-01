package com.xanghay.casamarmorista.controllers;

import com.xanghay.casamarmorista.dto.AnexoBoletoDTO;
import com.xanghay.casamarmorista.dto.EditarBoletoDTO;
import com.xanghay.casamarmorista.dto.VerBoletosSemAnexoDTO;
import com.xanghay.casamarmorista.models.Boletos;
import com.xanghay.casamarmorista.models.EnviarBoletosDTO;
import com.xanghay.casamarmorista.repositories.BoletosRepository;
import com.xanghay.casamarmorista.services.BoletosService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/boletos")
public class BoletosController {
    @Autowired
    private BoletosService boletosService;
    @Autowired BoletosRepository boletosRepository;

    @GetMapping
    public ResponseEntity<List<VerBoletosSemAnexoDTO>> pegarBoletos(){
        return ResponseEntity.ok().body(boletosService.pegarBoletos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VerBoletosSemAnexoDTO> pegarBoletoId(@PathVariable Integer id){
        return ResponseEntity.ok().body(boletosService.pegarBoletoPeloId(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Boletos> edicaoBoleto(@PathVariable Integer id, @RequestBody VerBoletosSemAnexoDTO dto) {
        boletosService.editarBoleto(id, dto);
        return ResponseEntity.ok().build();
    }
    @PostMapping
    public ResponseEntity<Boletos> registrarBoleto(@RequestBody EnviarBoletosDTO dto) {
        Boletos boletoSalvo = boletosService.salvarBoleto(dto);
        return ResponseEntity.ok(boletoSalvo);
    }

}

