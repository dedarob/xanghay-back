package com.xanghay.casamarmorista.repositories;

import com.xanghay.casamarmorista.dto.NotasDebitosQueryDTO;
import com.xanghay.casamarmorista.models.Debitos;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface DebitosRepository extends CrudRepository<Debitos, Long> {
    void deleteByNota_Id(Long notaId);
    List<Debitos> findByNota_IdIn(ArrayList<Integer> id);
    @Query("""
    SELECT n.id AS id, n.dataEmissao AS dataEmissao, d.valorTotal AS valorTotal
    FROM Notas n
    JOIN Debitos d ON d.nota.id = n.id
    WHERE n.cliente.id = :clienteId
""")
    List<NotasDebitosQueryDTO> buscarNotasComValor(@Param("clienteId") Long clienteId);


}
