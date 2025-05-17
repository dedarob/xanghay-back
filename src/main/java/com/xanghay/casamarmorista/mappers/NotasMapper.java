package com.xanghay.casamarmorista.mappers;

import com.xanghay.casamarmorista.dto.*;
import com.xanghay.casamarmorista.models.Debitos;
import com.xanghay.casamarmorista.models.Itens;
import com.xanghay.casamarmorista.models.Notas;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotasMapper {

    @Mapping(source = "cliente.id", target = "idCliente")
    @Mapping(source = "cliente.nomeCliente", target = "nomeCliente")
    @Mapping(source = "debitos.valorTotal", target = "valorTotal")
    NotasDetailedDTO toNotasDetailedDTO(Notas notas);
    List<NotasDetailedDTO> toNotasDetailedDTOList(List<Notas> notasList);

    List<CriarNotaDTO> toCriarNotaDTO(List<Notas> notas);
    Debitos toEntity(DebitosDTO dto);
    Itens toEntity(ItensDTO dto);
    Notas toEntity(NotaSimplesDTO dto);


}
