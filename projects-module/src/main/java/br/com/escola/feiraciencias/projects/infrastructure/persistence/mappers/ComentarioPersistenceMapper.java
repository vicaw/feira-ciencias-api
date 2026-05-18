package br.com.escola.feiraciencias.projects.infrastructure.persistence.mappers;

import br.com.escola.feiraciencias.projects.domain.model.Comentario;
import br.com.escola.feiraciencias.projects.infrastructure.persistence.entities.ComentarioJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface ComentarioPersistenceMapper {
    Comentario toDomain(ComentarioJpaEntity entity);
    ComentarioJpaEntity toEntity(Comentario domain);
}
