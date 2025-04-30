package com.xanghay.casamarmorista.mappers;

import com.xanghay.casamarmorista.dto.ClienteListDTO;
import com.xanghay.casamarmorista.models.Cliente;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClienteMapper {
    ClienteListDTO toDto(Cliente cliente);
    List<ClienteListDTO> toDtoList(List<Cliente> cliente);
}
