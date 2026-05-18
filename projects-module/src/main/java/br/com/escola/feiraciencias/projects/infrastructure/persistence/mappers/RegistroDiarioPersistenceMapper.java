package br.com.escola.feiraciencias.projects.infrastructure.persistence.mappers;

import br.com.escola.feiraciencias.projects.domain.model.RegistroDiario;
import br.com.escola.feiraciencias.projects.infrastructure.persistence.entities.RegistroDiarioJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface RegistroDiarioPersistenceMapper {
    RegistroDiario toDomain(RegistroDiarioJpaEntity entity);
    RegistroDiarioJpaEntity toEntity(RegistroDiario domain);
}
