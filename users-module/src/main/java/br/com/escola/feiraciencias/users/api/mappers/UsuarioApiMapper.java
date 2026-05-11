package br.com.escola.feiraciencias.users.api.mappers;

import br.com.escola.feiraciencias.users.api.requests.AlunoRequest;
import br.com.escola.feiraciencias.users.domain.model.Aluno;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface UsuarioApiMapper {
    Aluno toDomain(AlunoRequest dto);
    AlunoRequest toDto(Aluno domain);
}
