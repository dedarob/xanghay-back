package com.xanghay.casamarmorista.controllers;

import com.xanghay.casamarmorista.models.Pagamentos;
import com.xanghay.casamarmorista.services.PagamentosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/pagamentos")
@CrossOrigin("*")
public class PagamentosController {
    @Autowired
    private PagamentosService pagServ;

    @PostMapping("/{idCliente}")
    public ResponseEntity<Pagamentos> registrarPagamento(@RequestBody Pagamentos pagamento, @PathVariable Long idCliente){
        try {;
            return  ResponseEntity.ok().body(pagServ.enviarPag(pagamento, idCliente));
        }
        catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    @GetMapping("/sortado/{idCliente}")
    public ResponseEntity<?> pegarSaldoCliente(@PathVariable Long idCliente){
        return ResponseEntity.ok().body(pagServ.mostrarNotasComBaixa(idCliente));
    }
}
