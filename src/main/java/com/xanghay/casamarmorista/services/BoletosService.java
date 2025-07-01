package com.xanghay.casamarmorista.services;

import com.xanghay.casamarmorista.dto.AnexoBoletoDTO;
import com.xanghay.casamarmorista.dto.VerBoletosSemAnexoDTO;
import com.xanghay.casamarmorista.models.Boletos;
import com.xanghay.casamarmorista.models.EnviarBoletosDTO;
import com.xanghay.casamarmorista.repositories.BoletosRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BoletosService {

    @Autowired
    private BoletosRepository repo;
    @Autowired
    private DataSource dataSource;
    public List<VerBoletosSemAnexoDTO> pegarBoletos(){
        return repo.buscarBoletosSemAnexo();
    }
    public void editarBoleto(Integer id, VerBoletosSemAnexoDTO dto){
        Boletos boleto = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Boleto não encontrado"));

        boleto.setDescricao(dto.getDescricao());
        boleto.setDataVencimento(dto.getDataVencimento());
        boleto.setSituacao(dto.getSituacao());
        boleto.setValor(dto.getValor());
        boleto.setObservacoes(dto.getObservacoes());
        boleto.setDataPagamento(dto.getDataPagamento());
        boleto.setBanco(dto.getBanco());

        repo.save(boleto);
    }
    public VerBoletosSemAnexoDTO pegarBoletoPeloId(Integer id){
        return repo.buscarBoletosSemAnexoPeloId(id);
    }
    public Boletos salvarBoleto(EnviarBoletosDTO dto) {
        Boletos boleto = new Boletos();
        boleto.setDescricao(dto.getDescricao());
        boleto.setDataVencimento(dto.getDataVencimento());
        boleto.setSituacao(dto.getSituacao());
        boleto.setValor(dto.getValor());
        boleto.setObservacoes(dto.getObservacoes());
        boleto.setDataPagamento(dto.getDataPagamento());
        boleto.setBanco(dto.getBanco());


        return repo.save(boleto);
    }

}
