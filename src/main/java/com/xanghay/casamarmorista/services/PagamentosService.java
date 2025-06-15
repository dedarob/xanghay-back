package com.xanghay.casamarmorista.services;

import com.xanghay.casamarmorista.dto.NotasComPagamentosDTO;
import com.xanghay.casamarmorista.dto.NotasDebitosQueryDTO;
import com.xanghay.casamarmorista.dto.VinculoPagamentoDebitoDTO;
import com.xanghay.casamarmorista.models.Cliente;
import com.xanghay.casamarmorista.models.Notas;
import com.xanghay.casamarmorista.models.Pagamentos;
import com.xanghay.casamarmorista.repositories.ClienteRepository;
import com.xanghay.casamarmorista.repositories.DebitosRepository;
import com.xanghay.casamarmorista.repositories.NotasRepository;
import com.xanghay.casamarmorista.repositories.PagamentosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PagamentosService {
    @Autowired
    private PagamentosRepository pagRepo;
    @Autowired
    private ClienteRepository clienteRepo;
    @Autowired
    private DebitosRepository debRepo;
    @Autowired
    private NotasRepository notasRepo;


    public Pagamentos enviarPag(Pagamentos pagamento, Long idCliente){
        if(!clienteRepo.existsById(idCliente.intValue())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "cliente não encontrado");
        }
        Cliente cliente = clienteRepo.findById(idCliente.intValue()).get();
        pagamento.setCliente(cliente);
        pagRepo.save(pagamento);
        return pagamento;
    }

    public List<VinculoPagamentoDebitoDTO> darSortNosPagamentos(Long idCliente) {
        if (!clienteRepo.existsById(idCliente.intValue())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "cliente não encontrado");
        }

        List<Pagamentos> pagamentosList = pagRepo.findAllByCliente_Id(idCliente);
        List<NotasDebitosQueryDTO> notasComValor = debRepo.buscarNotasComValor(idCliente);

        pagamentosList.sort(Comparator.comparing(Pagamentos::getDataPagamento));
        notasComValor.sort(Comparator.comparing(NotasDebitosQueryDTO::getDataEmissao));

        List<VinculoPagamentoDebitoDTO> resultado = new ArrayList<>();
        BigDecimal sobra = BigDecimal.ZERO;
        Pagamentos pagamentoComSobra = null;
        int j = 0;

        for (NotasDebitosQueryDTO nota : notasComValor) {
            BigDecimal restanteNota = nota.getValorTotal();
            boolean notaPagaOuParcial = false;

            while (restanteNota.compareTo(BigDecimal.ZERO) > 0 && (j < pagamentosList.size() || sobra.compareTo(BigDecimal.ZERO) > 0)) {
                Pagamentos pagamento = (j < pagamentosList.size()) ? pagamentosList.get(j) : null;
                BigDecimal valorPagamento = (pagamento != null) ? pagamento.getValorPago() : BigDecimal.ZERO;
                BigDecimal valorUsado = valorPagamento.add(sobra);
                BigDecimal diferenca = restanteNota.subtract(valorUsado);

                VinculoPagamentoDebitoDTO dto = new VinculoPagamentoDebitoDTO();
                dto.setIdNota(nota.getId());
                dto.setDataNota(nota.getDataEmissao());
                dto.setValorDebito(nota.getValorTotal());

                if (!resultado.isEmpty()) {
                    dto.setValorAtualDebitoPosPagAnterior(resultado.get(resultado.size() - 1).getValorRestanteDebito());
                }

                if (pagamento != null) {
                    dto.setIdPagamento(pagamento.getId());
                    dto.setDataPagamento(pagamento.getDataPagamento());
                    dto.setValorPagoOriginal(pagamento.getValorPago());
                    pagamentoComSobra = pagamento;
                    dto.setIsSobraSomente(false);
                } else if (pagamentoComSobra != null) {
                    dto.setIdPagamento(pagamentoComSobra.getId());
                    dto.setDataPagamento(pagamentoComSobra.getDataPagamento());
                    dto.setValorPagoOriginal(pagamentoComSobra.getValorPago());
                    dto.setIsSobraSomente(true);
                }
                dto.setValorPago((pagamento != null) ? pagamento.getValorPago() : sobra);

                if (diferenca.compareTo(BigDecimal.ZERO) <= 0) {
                    sobra = diferenca.abs();
                    restanteNota = BigDecimal.ZERO;
                    dto.setValorRestanteDebito(BigDecimal.ZERO);
                    dto.setSituacao("PAGO");
                    resultado.add(dto);
                    notaPagaOuParcial = true;

                    if (pagamento != null) j++;
                    else sobra = BigDecimal.ZERO;
                } else {
                    sobra = BigDecimal.ZERO;
                    restanteNota = diferenca;
                    dto.setValorRestanteDebito(restanteNota);
                    dto.setSituacao("PARCIALMENTE PAGO");
                    resultado.add(dto);
                    notaPagaOuParcial = true;

                    if (pagamento != null) j++;
                }
            }

            if (!notaPagaOuParcial) {
                VinculoPagamentoDebitoDTO dto = new VinculoPagamentoDebitoDTO();
                dto.setIdNota(nota.getId());
                dto.setDataNota(nota.getDataEmissao());
                dto.setValorDebito(nota.getValorTotal());
                dto.setValorRestanteDebito(nota.getValorTotal());
                dto.setSituacao("EM ABERTO");
                dto.setIsSobraSomente(false);
                resultado.add(dto);
            }
        }

        return resultado;
    }




    //logica quebrada, refazer
    public List<VinculoPagamentoDebitoDTO> mostrarNotasComBaixa(Long idCliente) {
        if (!clienteRepo.existsById(idCliente.intValue())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "cliente não encontrado");
        }
        List<Pagamentos> pagamentosList = pagRepo.findAllByCliente_Id(idCliente);
        List<NotasDebitosQueryDTO> notasComValor = debRepo.buscarNotasComValor(idCliente);
        pagamentosList.sort(Comparator.comparing(Pagamentos::getDataPagamento));
        notasComValor.sort(Comparator.comparing(NotasDebitosQueryDTO::getDataEmissao));
        List<VinculoPagamentoDebitoDTO> resultado = new ArrayList<>();
        int pagamentoIndex = 0;
        BigDecimal usadoDoPagamentoAtual = BigDecimal.ZERO;
        for (NotasDebitosQueryDTO nota : notasComValor) {
            BigDecimal restanteDebito = nota.getValorTotal();
            while (restanteDebito.compareTo(BigDecimal.ZERO) > 0 && pagamentoIndex < pagamentosList.size()) {
                Pagamentos pagamentoAtual = pagamentosList.get(pagamentoIndex);
                BigDecimal restantePagamento = pagamentoAtual.getValorPago().subtract(usadoDoPagamentoAtual);

                if (restantePagamento.compareTo(BigDecimal.ZERO) <= 0) {
                    pagamentoIndex++;
                    usadoDoPagamentoAtual = BigDecimal.ZERO;
                    continue;
                }
                BigDecimal valorAlocado = restanteDebito.min(restantePagamento);
                restanteDebito = restanteDebito.subtract(valorAlocado);
                usadoDoPagamentoAtual = usadoDoPagamentoAtual.add(valorAlocado);
                VinculoPagamentoDebitoDTO dto = new VinculoPagamentoDebitoDTO();
                dto.setIdNota(nota.getId());
                dto.setDataNota(nota.getDataEmissao());
                dto.setIdDebito(nota.getIdDebito());
                dto.setValorDebito(nota.getValorTotal());
                dto.setValorPago(valorAlocado);
                dto.setDataPagamento(pagamentoAtual.getDataPagamento());
                dto.setValorRestanteDebito(restanteDebito);
                dto.setIdPagamento(pagamentoAtual.getId());

                resultado.add(dto);

                if (usadoDoPagamentoAtual.compareTo(pagamentoAtual.getValorPago()) == 0) {
                    pagamentoIndex++;
                    usadoDoPagamentoAtual = BigDecimal.ZERO;
                }
            }
        }

        return resultado;
    }


    public List<NotasComPagamentosDTO> entregarNotasComBaixa(Long idCliente) {
        if (!clienteRepo.existsById(idCliente.intValue())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "cliente não encontrado");
        }

        List<Pagamentos> pagamentosList = pagRepo.findAllByCliente_Id(idCliente);
        List<Notas> notasList = new ArrayList<>(notasRepo.findAllByCliente_Id(idCliente));
        notasList.sort(Comparator.comparing(Notas::getDataEmissao));

        List<NotasComPagamentosDTO> listDto = new ArrayList<>();

        int iPag = 0;
        BigDecimal valorRestantePagamento = BigDecimal.ZERO;

        for (Notas nota : notasList) {
            BigDecimal valorNota = nota.getDebitos().getValorTotal();

            NotasComPagamentosDTO dto = new NotasComPagamentosDTO();
            dto.setDataNota(nota.getDataEmissao());
            dto.setIdNota(nota.getId().longValue());
            dto.setValorPagoOriginal(valorNota);

            List<Long> pagamentosIds = new ArrayList<>();
            List<LocalDate> pagamentosDatas = new ArrayList<>();

            BigDecimal valorPagoNaNota = BigDecimal.ZERO;

            while (valorPagoNaNota.compareTo(valorNota) < 0 && iPag < pagamentosList.size()) {
                if (valorRestantePagamento.compareTo(BigDecimal.ZERO) <= 0) {
                    Pagamentos pagamentoAtual = pagamentosList.get(iPag);
                    valorRestantePagamento = pagamentoAtual.getValorPago();

                    pagamentosIds.add(pagamentoAtual.getId());
                    pagamentosDatas.add(pagamentoAtual.getDataPagamento());

                    iPag++;
                }

                BigDecimal valorFaltanteNota = valorNota.subtract(valorPagoNaNota);

                if (valorRestantePagamento.compareTo(valorFaltanteNota) >= 0) {

                    valorPagoNaNota = valorNota;
                    valorRestantePagamento = valorRestantePagamento.subtract(valorFaltanteNota);
                } else {

                    valorPagoNaNota = valorPagoNaNota.add(valorRestantePagamento);
                    valorRestantePagamento = BigDecimal.ZERO;
                }
            }

            BigDecimal sobra = valorPagoNaNota.subtract(valorNota);
            dto.setSobra(sobra);

            dto.setIdPagamentos(pagamentosIds);
            dto.setDataPagamentos(pagamentosDatas);

            if (pagamentosIds.isEmpty()) {
                dto.setSituacao("em aberta");
            } else if (valorPagoNaNota.compareTo(valorNota) == 0) {
                dto.setSituacao("paga");
            } else {
                dto.setSituacao("parcialmente paga");
            }

            listDto.add(dto);
        }

        return listDto;
    }





    public BigDecimal sortarPagamentos(Long idCliente){
        if(!clienteRepo.existsById(idCliente.intValue())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "cliente não encontrado");
        }

        List<Pagamentos> pagamentosList = pagRepo.findAllByCliente_Id(idCliente);
        List<Notas> notasList = notasRepo.findAllByCliente_Id(idCliente);

        List<BigDecimal> valoresFiltrados = new ArrayList<>();
        for (Notas nota : notasList) {
            valoresFiltrados.add(nota.getDebitos().getValorTotal());
        }

        BigDecimal valoresDebitoSomados = BigDecimal.ZERO;
        for (BigDecimal valor : valoresFiltrados) {
            valoresDebitoSomados = valoresDebitoSomados.add(valor);
        }

        BigDecimal valoresPagosSomados = BigDecimal.ZERO;
        for (Pagamentos pag : pagamentosList) {
            valoresPagosSomados = valoresPagosSomados.add(pag.getValorPago());
        }

        return valoresDebitoSomados.subtract(valoresPagosSomados);
    }
}
