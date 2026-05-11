package br.com.escola.feiraciencias.users.api.mappers;

import br.com.escola.feiraciencias.users.api.dto.requests.CadastrarAlunoRequest;
import br.com.escola.feiraciencias.users.domain.model.Aluno;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface UsuarioApiMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tipoUsuario", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "criadoPorId", ignore = true)
    Aluno toDomain(CadastrarAlunoRequest dto);

    CadastrarAlunoRequest toDto(Aluno domain);
}
