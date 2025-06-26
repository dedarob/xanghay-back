package com.xanghay.casamarmorista.services;

import com.xanghay.casamarmorista.dto.AnexoBoletoDTO;
import com.xanghay.casamarmorista.dto.VerBoletosSemAnexoDTO;
import com.xanghay.casamarmorista.models.Boletos;
import com.xanghay.casamarmorista.models.EnviarBoletosDTO;
import com.xanghay.casamarmorista.repositories.BoletosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public VerBoletosSemAnexoDTO pegarBoletoPeloId(Integer id){
        return repo.buscarBoletosSemAnexoPeloId(id);
    }
    public Boletos salvarBoleto(EnviarBoletosDTO dto, MultipartFile anexo) {
        Boletos boleto = new Boletos();
        boleto.setDescricao(dto.getDescricao());
        boleto.setDataVencimento(dto.getDataVencimento());
        boleto.setSituacao(dto.getSituacao());
        boleto.setValor(dto.getValor());
        boleto.setObservacoes(dto.getObservacoes());
        boleto.setDataPagamento(dto.getDataPagamento());
        boleto.setBanco(dto.getBanco());

        if (anexo != null && !anexo.isEmpty()) {
            try {
                boleto.setAnexo(anexo.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Erro ao processar anexo", e);
            }
        }

        return repo.save(boleto);
    }

    public AnexoBoletoDTO buscarAnexoPorIdJdbc(Integer id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, anexo FROM boletos WHERE id = ?")) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int boletoId = rs.getInt("id");
                byte[] anexo = rs.getBytes("anexo");
                return new AnexoBoletoDTO(boletoId, anexo);
            } else {
                throw new RuntimeException("Boleto não encontrado");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar anexo com JDBC", e);
        }
    }
}
