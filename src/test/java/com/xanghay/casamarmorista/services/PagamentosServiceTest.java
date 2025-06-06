package com.xanghay.casamarmorista.services;

import com.xanghay.casamarmorista.dto.NotasComPagamentosDTO;
import com.xanghay.casamarmorista.models.Debitos;
import com.xanghay.casamarmorista.models.Notas;
import com.xanghay.casamarmorista.models.Pagamentos;
import com.xanghay.casamarmorista.repositories.ClienteRepository;
import com.xanghay.casamarmorista.repositories.DebitosRepository;
import com.xanghay.casamarmorista.repositories.NotasRepository;
import com.xanghay.casamarmorista.repositories.PagamentosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PagamentosServiceTest {
    @Mock
    private PagamentosRepository pagRepo;
    @Mock
    private ClienteRepository clienteRepo;
    @Mock
    private DebitosRepository debRepo;
    @Mock
    private NotasRepository notasRepo;

    @Autowired
    @InjectMocks
    private PagamentosService pagServ;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("se tudo tá ok salva diminui debitos de acordo com valor de pagamentos")
    void sortarPagamentosCase1() {
        Long clienteId = 1L;
        Mockito.when(clienteRepo.existsById(clienteId.intValue())).thenReturn(true);

        Notas nota = new Notas();
        Debitos debito = new Debitos();
        debito.setValorTotal(new BigDecimal("100.50"));
        nota.setDebitos(debito);

        Mockito.when(notasRepo.findAllByCliente_Id(clienteId))
                .thenReturn(List.of(nota));

        Mockito.when(pagRepo.findAllByCliente_Id(clienteId))
                .thenReturn(List.of());

        BigDecimal resultado = pagServ.sortarPagamentos(clienteId);
        assertEquals(new BigDecimal("100.50"), resultado);
    }

    @Test
    @DisplayName("joga exception se cliente não existe")
    void sortarPagamentosCase2() {
        Long clienteId = 1L;
        Mockito.when(clienteRepo.existsById(clienteId.intValue())).thenReturn(false);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            pagServ.sortarPagamentos(clienteId);
        });
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("cliente não encontrado", ex.getReason());
    }
    @Test
    @DisplayName("processa múltiplas notas e múltiplos pagamentos corretamente")
    void sortarPagamentosCase3() {
        Long clienteId = 1L;
        Mockito.when(clienteRepo.existsById(clienteId.intValue())).thenReturn(true);

        Notas nota1 = new Notas();
        Debitos debito1 = new Debitos();
        debito1.setValorTotal(new BigDecimal("100.00"));
        nota1.setDebitos(debito1);

        Notas nota2 = new Notas();
        Debitos debito2 = new Debitos();
        debito2.setValorTotal(new BigDecimal("200.00"));
        nota2.setDebitos(debito2);

        Notas nota3 = new Notas();
        Debitos debito3 = new Debitos();
        debito3.setValorTotal(new BigDecimal("300.00"));
        nota3.setDebitos(debito3);

        Mockito.when(notasRepo.findAllByCliente_Id(clienteId))
                .thenReturn(List.of(nota1, nota2, nota3));

        Pagamentos pagamento1 = new Pagamentos();
        pagamento1.setValorPago(new BigDecimal("150.00"));

        Pagamentos pagamento2 = new Pagamentos();
        pagamento2.setValorPago(new BigDecimal("100.00"));

        Pagamentos pagamento3 = new Pagamentos();
        pagamento3.setValorPago(new BigDecimal("50.00"));

        Mockito.when(pagRepo.findAllByCliente_Id(clienteId))
                .thenReturn(List.of(pagamento1, pagamento2, pagamento3));

        BigDecimal resultado = pagServ.sortarPagamentos(clienteId);

        assertEquals(new BigDecimal("300.00"), resultado);
    }

    @Test
    @DisplayName("caso 1: cliente não existe")
    void entregarNotasComBaixa_clienteInexistente() {
        Long clienteId = 1L;
        Mockito.when(clienteRepo.existsById(clienteId.intValue())).thenReturn(false);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            pagServ.entregarNotasComBaixa(clienteId);
        });

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("cliente não encontrado", ex.getReason());
    }

    @Test
    @DisplayName("caso 2: cliente pagou exatamente uma nota")
    void entregarNotasComBaixa_pagamentoExato() {
        Long clienteId = 1L;
        Mockito.when(clienteRepo.existsById(clienteId.intValue())).thenReturn(true);

        Notas nota = new Notas();
        nota.setId(1);
        nota.setDataEmissao(LocalDate.now());
        Debitos debito = new Debitos();
        debito.setValorTotal(new BigDecimal("100.00"));
        nota.setDebitos(debito);
        Mockito.when(notasRepo.findAllByCliente_Id(clienteId)).thenReturn(List.of(nota));

        Pagamentos pag = new Pagamentos();
        pag.setId(1L);
        pag.setValorPago(new BigDecimal("100.00"));
        Mockito.when(pagRepo.findAllByCliente_Id(clienteId)).thenReturn(List.of(pag));

        List<NotasComPagamentosDTO> resultado = pagServ.entregarNotasComBaixa(clienteId);

        assertEquals(1, resultado.size());
        assertEquals(new BigDecimal("0.00"), resultado.get(0).getSobra());
    }

    @Test
    @DisplayName("caso 3: cliente pagou mais do que a nota (sobra)")
    void entregarNotasComBaixa_pagamentoMaiorQueNota() {
        Long clienteId = 1L;
        Mockito.when(clienteRepo.existsById(clienteId.intValue())).thenReturn(true);

        Notas nota = new Notas();
        nota.setId(1);
        nota.setDataEmissao(LocalDate.now());
        Debitos debito = new Debitos();
        debito.setValorTotal(new BigDecimal("100.00"));
        nota.setDebitos(debito);
        Mockito.when(notasRepo.findAllByCliente_Id(clienteId)).thenReturn(List.of(nota));

        Pagamentos pag = new Pagamentos();
        pag.setId(1L);
        pag.setValorPago(new BigDecimal("150.00"));
        Mockito.when(pagRepo.findAllByCliente_Id(clienteId)).thenReturn(List.of(pag));

        List<NotasComPagamentosDTO> resultado = pagServ.entregarNotasComBaixa(clienteId);

        assertEquals(1, resultado.size());
        assertEquals(new BigDecimal("50.00"), resultado.get(0).getSobra());
    }

    @Test
    @DisplayName("caso 4: cliente pagou menos do que devia (falta pagar)")
    void entregarNotasComBaixa_pagamentoMenorQueNota() {
        Long clienteId = 1L;
        Mockito.when(clienteRepo.existsById(clienteId.intValue())).thenReturn(true);

        Notas nota = new Notas();
        nota.setId(1);
        nota.setDataEmissao(LocalDate.now());
        Debitos debito = new Debitos();
        debito.setValorTotal(new BigDecimal("200.00"));
        nota.setDebitos(debito);
        Mockito.when(notasRepo.findAllByCliente_Id(clienteId)).thenReturn(List.of(nota));

        Pagamentos pag = new Pagamentos();
        pag.setId(1L);
        pag.setValorPago(new BigDecimal("120.00"));
        Mockito.when(pagRepo.findAllByCliente_Id(clienteId)).thenReturn(List.of(pag));

        List<NotasComPagamentosDTO> resultado = pagServ.entregarNotasComBaixa(clienteId);

        assertEquals(1, resultado.size());
        assertEquals(new BigDecimal("-80.00"), resultado.get(0).getSobra());
    }

    @Test
    @DisplayName("caso 5: cliente pagou mais de uma nota, sobra do 1° vai pra 2°")
    void entregarNotasComBaixa_comSobraReaproveitada() {
        Long clienteId = 1L;
        Mockito.when(clienteRepo.existsById(clienteId.intValue())).thenReturn(true);



        Notas nota1 = new Notas();
        nota1.setId(1);
        nota1.setDataEmissao(LocalDate.of(2024, 1, 1));
        Debitos debito1 = new Debitos();
        debito1.setValorTotal(new BigDecimal("100.00"));
        nota1.setDebitos(debito1);

        Notas nota2 = new Notas();
        nota2.setId(2);
        nota2.setDataEmissao(LocalDate.of(2024, 2, 1));
        Debitos debito2 = new Debitos();
        debito2.setValorTotal(new BigDecimal("50.00"));
        nota2.setDebitos(debito2);

        Mockito.when(notasRepo.findAllByCliente_Id(clienteId)).thenReturn(List.of(nota1, nota2));

        Pagamentos pag1 = new Pagamentos();
        pag1.setId(1L);
        pag1.setValorPago(new BigDecimal("130.00"));

        Pagamentos pag2 = new Pagamentos();
        pag2.setId(2L);
        pag2.setValorPago(new BigDecimal("30.00"));

        Mockito.when(pagRepo.findAllByCliente_Id(clienteId)).thenReturn(List.of(pag1, pag2));

        List<NotasComPagamentosDTO> resultado = pagServ.entregarNotasComBaixa(clienteId);

        assertEquals(2, resultado.size());
        assertEquals(new BigDecimal("30.00"), resultado.get(0).getSobra());
        assertEquals(new BigDecimal("10.00"), resultado.get(1).getSobra());
    }


}