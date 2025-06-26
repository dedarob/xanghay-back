package com.xanghay.casamarmorista.controllers;

import com.xanghay.casamarmorista.dto.AnexoBoletoDTO;
import com.xanghay.casamarmorista.dto.VerBoletosSemAnexoDTO;
import com.xanghay.casamarmorista.models.Boletos;
import com.xanghay.casamarmorista.models.EnviarBoletosDTO;
import com.xanghay.casamarmorista.repositories.BoletosRepository;
import com.xanghay.casamarmorista.services.BoletosService;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Boletos> registrarBoleto(
            @RequestPart("boleto") EnviarBoletosDTO dto,
            @RequestPart(value = "anexo", required = false) MultipartFile anexo
    ) {
        Boletos boletoSalvo = boletosService.salvarBoleto(dto, anexo);
        return ResponseEntity.ok(boletoSalvo);
    }

    @GetMapping("/anexo/{id}")
    public ResponseEntity<byte[]> downloadAnexo(@PathVariable Integer id) {
        AnexoBoletoDTO dto = boletosService.buscarAnexoPorIdJdbc(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"boleto_" + dto.getId() + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(dto.getAnexo());
    }
}

