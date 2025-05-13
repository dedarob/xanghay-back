package com.xanghay.casamarmorista.mappers;

import com.xanghay.casamarmorista.dto.NotasDetailedDTO;
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
}
