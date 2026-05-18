package br.com.escola.feiraciencias.events.infrastructure.persistence.mappers;

import br.com.escola.feiraciencias.events.domain.model.Evento;
import br.com.escola.feiraciencias.events.infrastructure.persistence.entities.EventoJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface EventoPersistenceMapper {

    Evento toDomain(EventoJpaEntity entity);

    EventoJpaEntity toEntity(Evento domain);
}
