package com.xanghay.casamarmorista.services;

import com.xanghay.casamarmorista.dto.NotasDebitosQueryDTO;
import com.xanghay.casamarmorista.dto.VinculoPagamentoDebitoDTO;
import com.xanghay.casamarmorista.models.Cliente;
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

    //darSortNosPagamentos funciona
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




}
