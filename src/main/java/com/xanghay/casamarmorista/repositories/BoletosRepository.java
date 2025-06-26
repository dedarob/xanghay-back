package com.xanghay.casamarmorista.repositories;

import com.xanghay.casamarmorista.dto.AnexoBoletoDTO;
import com.xanghay.casamarmorista.dto.VerBoletosSemAnexoDTO;
import com.xanghay.casamarmorista.models.Boletos;
import com.xanghay.casamarmorista.models.Cliente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoletosRepository extends CrudRepository<Boletos, Integer> {
    @Query("""
        SELECT new com.xanghay.casamarmorista.dto.VerBoletosSemAnexoDTO(
            b.id, b.descricao, b.dataVencimento, b.situacao,
            b.valor, b.observacoes, b.dataPagamento, b.banco
        )
        FROM Boletos b
    """)
    List<VerBoletosSemAnexoDTO> buscarBoletosSemAnexo();

    @Query("""
    SELECT new com.xanghay.casamarmorista.dto.VerBoletosSemAnexoDTO(
        b.id, b.descricao, b.dataVencimento, b.situacao,
        b.valor, b.observacoes, b.dataPagamento, b.banco
    )
    FROM Boletos b
    WHERE b.id = :id
""")
    VerBoletosSemAnexoDTO buscarBoletosSemAnexoPeloId(@Param("id") Integer id);
    /*@Query("""
    SELECT new com.xanghay.casamarmorista.dto.AnexoBoletoDTO(b.id, b.anexo)
    FROM Boletos b
    WHERE b.id = :id
""")
    AnexoBoletoDTO buscarAnexoPorId(@Param("id") Integer id);
    */


}
