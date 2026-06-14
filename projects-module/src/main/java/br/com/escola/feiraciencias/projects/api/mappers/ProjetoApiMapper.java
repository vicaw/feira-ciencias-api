package br.com.escola.feiraciencias.projects.api.mappers;

import br.com.escola.feiraciencias.projects.api.dto.requests.CriarProjetoRequest;
import br.com.escola.feiraciencias.projects.api.dto.requests.AtualizarProjetoRequest;
import br.com.escola.feiraciencias.projects.api.dto.requests.CriarComentarioRequest;
import br.com.escola.feiraciencias.projects.api.dto.requests.CriarRegistroDiarioRequest;
import br.com.escola.feiraciencias.projects.api.dto.responses.ProjetoResponse;
import br.com.escola.feiraciencias.projects.api.dto.responses.ComentarioResponse;
import br.com.escola.feiraciencias.projects.api.dto.responses.RegistroDiarioResponse;
import br.com.escola.feiraciencias.projects.domain.model.Projeto;
import br.com.escola.feiraciencias.projects.domain.model.Comentario;
import br.com.escola.feiraciencias.projects.domain.model.RegistroDiario;
import br.com.escola.feiraciencias.shared.domain.enums.SituacaoProjeto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface ProjetoApiMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "situacao", expression = "java(br.com.escola.feiraciencias.shared.domain.enums.SituacaoProjeto.ATIVO)")
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "criadoPorId", ignore = true)
    Projeto toDomain(CriarProjetoRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "criadoPorId", ignore = true)
    Projeto toDomain(AtualizarProjetoRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataComentario", ignore = true)
    @Mapping(target = "criadoPorId", ignore = true)
    @Mapping(target = "projetoId", ignore = true)
    Comentario toDomain(CriarComentarioRequest dto);

    ProjetoResponse toResponse(Projeto domain);
    ComentarioResponse toResponse(Comentario domain);
}
