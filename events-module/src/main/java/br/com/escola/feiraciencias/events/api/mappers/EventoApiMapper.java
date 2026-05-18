package br.com.escola.feiraciencias.events.api.mappers;

import br.com.escola.feiraciencias.events.api.dto.requests.CriarEventoRequest;
import br.com.escola.feiraciencias.events.api.dto.requests.AtualizarEventoRequest;
import br.com.escola.feiraciencias.events.api.dto.responses.EventoResponse;
import br.com.escola.feiraciencias.events.domain.model.Evento;
import br.com.escola.feiraciencias.shared.domain.enums.SituacaoEvento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import java.time.LocalDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface EventoApiMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "situacao", expression = "java(br.com.escola.feiraciencias.shared.domain.enums.SituacaoEvento.ATIVO)")
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "criadoPorId", ignore = true)
    Evento toDomain(CriarEventoRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "criadoPorId", ignore = true)
    Evento toDomain(AtualizarEventoRequest dto);

    EventoResponse toResponse(Evento domain);
}
