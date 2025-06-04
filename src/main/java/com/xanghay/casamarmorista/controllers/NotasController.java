package com.xanghay.casamarmorista.controllers;

import com.xanghay.casamarmorista.dto.CriarNotaDTO;
import com.xanghay.casamarmorista.dto.ItensDTO;
import com.xanghay.casamarmorista.dto.NotasDetailedDTO;
import com.xanghay.casamarmorista.models.Itens;
import com.xanghay.casamarmorista.models.Notas;
import com.xanghay.casamarmorista.services.NotasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @GetMapping("itens/{id}")
    public ResponseEntity<List<Itens>> listItensPeloIdNota(@PathVariable Long id){
        List<Itens> itens = service.procurarItensPeloIdNota(id);
        return ResponseEntity.ok().body(itens);
    }

    @PostMapping
    public ResponseEntity<Notas> inserirNotasComItens(@RequestBody CriarNotaDTO nota){
        System.out.println("Recebido dataEmissao: " + nota.getNotaSimples().getDataEmissao());
        Notas notaSalva = service.registrarNotasComItens(nota);
        return ResponseEntity.status(201).body(notaSalva);
    }

    @PostMapping("/colocar-item/{id}")
    public ResponseEntity<Itens> addItemPorNota(@RequestBody ItensDTO dto, @PathVariable Long id){
        System.out.println("postman connect");
        return ResponseEntity.status(201).body(service.adicionarItemPorNota(dto, id));
    }

    @PutMapping("/modificar-item/{idNota}/item/{idItem}")
    public ResponseEntity<Itens> modificarItemPorNota(@RequestBody Itens itensCru, @PathVariable Long idNota, @PathVariable Long idItem){
        return ResponseEntity.status(200).body(service.modificarItensPorNota(itensCru, idNota, idItem));
    }

    @DeleteMapping("/deletar-item/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id){
        try {
            service.deletarItem(id);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "erro ao deletar item");
        }
    }

    @DeleteMapping("/deletar-nota/{id}")
    public ResponseEntity<Void> deletarNota(@PathVariable Long id){
        try {service.deleteNota(id);
            return ResponseEntity.noContent().build();}
        catch (ResponseStatusException e){
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }


}
