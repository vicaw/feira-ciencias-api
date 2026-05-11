package br.com.escola.feiraciencias.users.infrastructure.persistence.mappers;

import br.com.escola.feiraciencias.users.domain.model.Aluno;
import br.com.escola.feiraciencias.users.domain.model.Professor;
import br.com.escola.feiraciencias.users.domain.model.Usuario;
import br.com.escola.feiraciencias.users.infrastructure.persistence.entities.AlunoJpaEntity;
import br.com.escola.feiraciencias.users.infrastructure.persistence.entities.ProfessorJpaEntity;
import br.com.escola.feiraciencias.users.infrastructure.persistence.entities.UsuarioJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface UsuarioPersistenceMapper {
    Aluno toDomain(AlunoJpaEntity entity);
    Professor toDomain(ProfessorJpaEntity entity);

    AlunoJpaEntity toEntity(Aluno domain);
    ProfessorJpaEntity toEntity(Professor domain);

    default Usuario toDomainBase(UsuarioJpaEntity entity) {
        if (entity instanceof AlunoJpaEntity alunoEntity) {
            return toDomain(alunoEntity);
        } else if (entity instanceof ProfessorJpaEntity profEntity) {
            return toDomain(profEntity);
        }
        throw new IllegalArgumentException("Unknown entity type: " + entity.getClass().getName());
    }
}
