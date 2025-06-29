package com.xanghay.casamarmorista.services;

import com.xanghay.casamarmorista.dto.CriarNotaDTO;
import com.xanghay.casamarmorista.dto.ItensDTO;
import com.xanghay.casamarmorista.dto.NotasDetailedDTO;
import com.xanghay.casamarmorista.mappers.NotasMapper;
import com.xanghay.casamarmorista.models.Cliente;
import com.xanghay.casamarmorista.models.Debitos;
import com.xanghay.casamarmorista.models.Itens;
import com.xanghay.casamarmorista.models.Notas;
import com.xanghay.casamarmorista.repositories.DebitosRepository;
import com.xanghay.casamarmorista.repositories.ItensRepository;
import com.xanghay.casamarmorista.repositories.NotasRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotasService {

    private static final Logger logger = LoggerFactory.getLogger(NotasService.class);

    @Autowired
    private NotasRepository notaRepo;
    @Autowired
    private NotasMapper notasMapper;
    @Autowired
    private ItensRepository itensRepository;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private  DebitosRepository debitosRepository;

    public List<Notas> procurarNotasPeloIdDoCliente(Long clienteId){
        return notaRepo.findByCliente_Id(clienteId);
    }

    public List<NotasDetailedDTO> procurarListaDeNotasPeloIdDoCliente(Long clienteId){
        List<Notas> notasList = notaRepo.findByCliente_Id(clienteId);
        List<NotasDetailedDTO> notasDetailedDTOList = notasMapper.toNotasDetailedDTOList(notasList);
        return notasDetailedDTOList;
    }

    public List<Itens> procurarItensPeloIdNota(Long notaId){
        List<Itens> itensList = itensRepository.findByNota_Id(notaId);
        return itensList;
    }
    @Transactional
    public Notas registrarNotasComItens(CriarNotaDTO dto){
            Notas nota = notasMapper.toEntity(dto.getNotaSimples());
            Cliente cliente = clienteService.findById(dto.getNotaSimples().getClienteId());
            nota.setCliente(cliente);

            List<Itens> itens = dto.getItens().stream()
                    .map(notasMapper::toEntity)
                    .peek(item -> item.setNota(nota))
                    .toList();
            nota.setItens(itens.stream().collect(Collectors.toSet()));

            Debitos debito = new Debitos();
            debito.setNota(nota);
            debito.setValorTotal(BigDecimal.ZERO); //esse valor tá vindo do trigger no sql, entao deixe zerado
            nota.setDebitos(debito);
            return notaRepo.save(nota);
        }

        public Itens adicionarItemPorNota(ItensDTO dto, Long id){
            Optional<Notas> notaOptional = notaRepo.findById(id.intValue());
            if (!notaOptional.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "nota nao encontrada");
            } else {
                Notas nota = notaOptional.get();
                dto.setNotaId(nota.getId().longValue());
                Itens item = notasMapper.toEntity(dto);
                item.setNota(nota);
                itensRepository.save(item);
                logger.info("item criado com sucesso: {}", item);
                return item;
            }

        }

        public Itens modificarItensPorNota(Itens itensCru, Long idNota, Long idItem){

        Optional<Itens> itemOptional = itensRepository.findById(idItem);
        if(!itemOptional.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "item não encontrado");

        }
        Itens itensParaNota = itemOptional.get();
        Integer notaIdDoBD = itensParaNota.getNotaId();
        Optional<Notas> notaIdDoBd = notaRepo.findById(notaIdDoBD);
        if (!notaIdDoBd.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "nota não existe");
        }
        Notas nota = notaIdDoBd.get();
        itensCru.setId(idItem);
        itensCru.setNota(nota);
        if (!nota.getId().equals(itensCru.getNotaId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id da nota e chave estrangeira na tabela item não batem");
        }
        itensRepository.save(itensCru);
        return itensCru;
        }

        public void deletarItem(Long id){
        if (!itensRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "item com esse id não existe");
        }
        itensRepository.deleteById(id);
        }
        @Transactional
        public void deleteNota(Long id){
        if (!notaRepo.existsById(id.intValue())){
            System.out.println("maneeeeeee");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "nota com esse id não existe");
        }
        itensRepository.deleteAllByNota_Id(id);
        debitosRepository.deleteByNota_Id(id);
        notaRepo.deleteById(id.intValue());
        System.out.println("foifoifoi");
        }
}