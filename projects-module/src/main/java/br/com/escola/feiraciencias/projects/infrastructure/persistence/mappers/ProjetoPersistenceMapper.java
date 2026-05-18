package br.com.escola.feiraciencias.projects.infrastructure.persistence.mappers;

import br.com.escola.feiraciencias.projects.domain.model.Projeto;
import br.com.escola.feiraciencias.projects.domain.model.Comentario;
import br.com.escola.feiraciencias.projects.domain.model.RegistroDiario;
import br.com.escola.feiraciencias.projects.infrastructure.persistence.entities.ProjetoJpaEntity;
import br.com.escola.feiraciencias.projects.infrastructure.persistence.entities.ComentarioJpaEntity;
import br.com.escola.feiraciencias.projects.infrastructure.persistence.entities.RegistroDiarioJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface ProjetoPersistenceMapper {

    Projeto toDomain(ProjetoJpaEntity entity);
    ProjetoJpaEntity toEntity(Projeto domain);

    Comentario toCommentDomain(ComentarioJpaEntity entity);
    ComentarioJpaEntity toCommentEntity(Comentario domain);

    RegistroDiario toRegistroDomain(RegistroDiarioJpaEntity entity);
    RegistroDiarioJpaEntity toRegistroEntity(RegistroDiario domain);
}
