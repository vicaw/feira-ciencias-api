package br.com.escola.feiraciencias.projects.api.resources;

import br.com.escola.feiraciencias.projects.api.dto.requests.CriarProjetoRequest;
import br.com.escola.feiraciencias.projects.api.dto.requests.AtualizarProjetoRequest;
import br.com.escola.feiraciencias.projects.api.dto.requests.AtualizarProjetoMateriaisDescricaoRequest;
import br.com.escola.feiraciencias.projects.api.dto.requests.CriarComentarioRequest;
import br.com.escola.feiraciencias.projects.api.dto.requests.CriarRegistroDiarioRequest;
import br.com.escola.feiraciencias.projects.api.dto.requests.AdicionarIntegranteRequest;
import br.com.escola.feiraciencias.projects.api.dto.responses.ProjetoResponse;
import br.com.escola.feiraciencias.projects.api.dto.responses.ComentarioResponse;
import br.com.escola.feiraciencias.projects.api.dto.responses.RegistroDiarioResponse;
import br.com.escola.feiraciencias.projects.api.dto.responses.IntegranteResponse;
import br.com.escola.feiraciencias.projects.api.mappers.ProjetoApiMapper;
import br.com.escola.feiraciencias.projects.application.usecases.GestaoProjetoUseCase;
import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import java.util.List;
import java.util.stream.Collectors;

@Path("/projetos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProjetoResource {

    @Inject
    GestaoProjetoUseCase gestaoProjetoUseCase;

    @Inject
    ProjetoApiMapper mapper;

    @Inject
    JsonWebToken jwt;

    // ==================== PROJETOS CRUD ====================

    @POST
    @RolesAllowed("PROFESSOR")
    public Response criarProjeto(@Valid CriarProjetoRequest request) {
        Integer professorId = Integer.parseInt(jwt.getSubject());
        var projeto = mapper.toDomain(request);
        var criado = gestaoProjetoUseCase.criarProjeto(projeto, professorId);
        return Response.status(Response.Status.CREATED).entity(mapper.toResponse(criado)).build();
    }

    @GET
    @Path("/{id}")
    public Response obterProjeto(@PathParam("id") Integer id) {
        var projeto = gestaoProjetoUseCase.buscarProjetoPorId(id);
        return Response.ok(mapper.toResponse(projeto)).build();
    }

    @GET
    @Path("/evento/{eventoId}")
    public Response listarProjetosPorEvento(@PathParam("eventoId") Integer eventoId) {
        List<ProjetoResponse> projetos = gestaoProjetoUseCase.listarProjetosPorEvento(eventoId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return Response.ok(projetos).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("PROFESSOR")
    public Response atualizarProjeto(@PathParam("id") Integer id, @Valid AtualizarProjetoRequest request) {
        Integer professorId = Integer.parseInt(jwt.getSubject());
        var projeto = mapper.toDomain(request);
        var atualizado = gestaoProjetoUseCase.atualizarProjeto(id, projeto, professorId);
        return Response.ok(mapper.toResponse(atualizado)).build();
    }

    @PATCH
    @Path("/{id}/materiais-descricao")
    public Response atualizarMateriaisDescricao(@PathParam("id") Integer id, 
                                               @Valid AtualizarProjetoMateriaisDescricaoRequest request) {
        Integer usuarioId = Integer.parseInt(jwt.getSubject());
        var atualizado = gestaoProjetoUseCase.atualizarMateriaisDescricao(id, 
                                                                         request.descricao(), 
                                                                         request.materiais(), 
                                                                         usuarioId);
        return Response.ok(mapper.toResponse(atualizado)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("PROFESSOR")
    public Response excluirProjeto(@PathParam("id") Integer id) {
        Integer professorId = Integer.parseInt(jwt.getSubject());
        gestaoProjetoUseCase.excluirProjeto(id, professorId);
        return Response.noContent().build();
    }

    // ==================== INTEGRANTES ====================

    @POST
    @Path("/{id}/integrantes")
    @RolesAllowed("PROFESSOR")
    public Response adicionarIntegrante(@PathParam("id") Integer id, @Valid AdicionarIntegranteRequest request) {
        Integer professorId = Integer.parseInt(jwt.getSubject());
        var integrante = gestaoProjetoUseCase.adicionarIntegrante(id, request.usuarioId(), request.tipoIntegrante(), professorId);
        return Response.status(Response.Status.CREATED)
                .entity(new IntegranteResponse(integrante.getId(), 
                                              integrante.getProjetoId(), 
                                              integrante.getUsuarioId(),
                                              integrante.getTipoIntegrante().name(),
                                              integrante.getDataVinculo()))
                .build();
    }

    @GET
    @Path("/{id}/integrantes")
    public Response listarIntegrantes(@PathParam("id") Integer id) {
        List<IntegranteResponse> integrantes = gestaoProjetoUseCase.listarIntegrantes(id).stream()
                .map(i -> new IntegranteResponse(i.getId(), i.getProjetoId(), i.getUsuarioId(), 
                                                i.getTipoIntegrante().name(), i.getDataVinculo()))
                .collect(Collectors.toList());
        return Response.ok(integrantes).build();
    }

    // ==================== COMENTÁRIOS ====================

    @POST
    @Path("/{id}/comentarios")
    @RolesAllowed({"PROFESSOR", "ALUNO"})
    public Response adicionarComentario(@PathParam("id") Integer id, @Valid CriarComentarioRequest request) {
        Integer usuarioId = Integer.parseInt(jwt.getSubject());
        var comentario = mapper.toDomain(request);
        var criado = gestaoProjetoUseCase.adicionarComentario(id, comentario, usuarioId);
        return Response.status(Response.Status.CREATED).entity(mapper.toResponse(criado)).build();
    }

    @GET
    @Path("/{id}/comentarios")
    public Response listarComentarios(@PathParam("id") Integer id) {
        List<ComentarioResponse> comentarios = gestaoProjetoUseCase.listarComentarios(id).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return Response.ok(comentarios).build();
    }

    // ==================== REGISTROS DIÁRIOS ====================

    @POST
    @Path("/{id}/registros-diarios")
    @RolesAllowed("ALUNO")
    public Response criarRegistroDiario(@PathParam("id") Integer id, @Valid CriarRegistroDiarioRequest request) {
        Integer alunoId = Integer.parseInt(jwt.getSubject());
        var registro = mapper.toDomain(request);
        var criado = gestaoProjetoUseCase.criarRegistroDiario(id, registro, alunoId);
        return Response.status(Response.Status.CREATED).entity(mapper.toResponse(criado)).build();
    }

    @GET
    @Path("/{id}/registros-diarios")
    @RolesAllowed({"PROFESSOR", "ALUNO"})
    public Response listarRegistrosDiarios(@PathParam("id") Integer id) {
        List<RegistroDiarioResponse> registros = gestaoProjetoUseCase.listarRegistrosDiarios(id).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return Response.ok(registros).build();
    }
}
