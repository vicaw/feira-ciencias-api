package br.com.escola.feiraciencias.projects.api.resources;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import br.com.escola.feiraciencias.projects.api.dto.requests.AdicionarIntegranteRequest;
import br.com.escola.feiraciencias.projects.api.dto.requests.AtualizarProjetoMateriaisDescricaoRequest;
import br.com.escola.feiraciencias.projects.api.dto.requests.AtualizarProjetoRequest;
import br.com.escola.feiraciencias.projects.api.dto.requests.CriarComentarioRequest;
import br.com.escola.feiraciencias.projects.api.dto.requests.CriarProjetoRequest;
import br.com.escola.feiraciencias.projects.api.dto.requests.CriarRegistroDiarioRequest;
import br.com.escola.feiraciencias.projects.api.dto.responses.ComentarioResponse;
import br.com.escola.feiraciencias.projects.api.dto.responses.IntegranteResponse;
import br.com.escola.feiraciencias.projects.api.dto.responses.ProjetoResponse;
import br.com.escola.feiraciencias.projects.api.dto.responses.RegistroDiarioArquivoResponse;
import br.com.escola.feiraciencias.projects.api.dto.responses.RegistroDiarioResponse;
import br.com.escola.feiraciencias.projects.api.mappers.ProjetoApiMapper;
import br.com.escola.feiraciencias.projects.application.usecases.GestaoProjetoUseCase;
import br.com.escola.feiraciencias.projects.domain.model.RegistroDiario;
import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.storage.application.contracts.StorageService;
import br.com.escola.feiraciencias.storage.application.dto.StorageFileInput;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/projetos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProjetoResource {

    @Inject
    GestaoProjetoUseCase gestaoProjetoUseCase;

    @Inject
    ProjetoApiMapper mapper;

    @Inject
    StorageService storageService;

    @Inject
    JsonWebToken jwt;

    // ==================== PROJETOS CRUD ====================

    @POST
    @RolesAllowed({"ADMIN","PROFESSOR"})
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
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RolesAllowed("ALUNO")
    public Response criarRegistroDiario(
            @PathParam("id") Integer projetoId,
            @Valid CriarRegistroDiarioRequest request) {
        
        Integer alunoId = Integer.parseInt(jwt.getSubject());
        
        RegistroDiario registro = new RegistroDiario();
        registro.setTexto(request.texto);

        List<StorageFileInput> arquivosInput = new ArrayList<>();
        if (request.arquivos != null) {
            for (FileUpload fileUpload : request.arquivos) {
                StorageFileInput input = extrairFileInput(fileUpload, "projects/" + projetoId + "/registros");
                if (input != null) {
                    arquivosInput.add(input);
                }
            }
        }

        var criado = gestaoProjetoUseCase.criarRegistroDiario(projetoId, registro, alunoId, arquivosInput);
        return Response.status(Response.Status.CREATED).entity(toRegistroDiarioResponse(criado)).build();
    }

    @GET
    @Path("/{id}/registros-diarios")
    @RolesAllowed({"PROFESSOR", "ALUNO"})
    public Response listarRegistrosDiarios(@PathParam("id") Integer id) {
        List<RegistroDiarioResponse> registros = gestaoProjetoUseCase.listarRegistrosDiarios(id).stream()
                .map(this::toRegistroDiarioResponse)
                .collect(Collectors.toList());
        return Response.ok(registros).build();
    }

    @POST
    @Path("/{id}/registros-diarios/{registroId}/arquivos")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RolesAllowed("ALUNO")
    public Response adicionarArquivoRegistro(
            @PathParam("id") Integer projetoId,
            @PathParam("registroId") Integer registroId,
            @RestForm("arquivo") FileUpload arquivo) {
        
        Integer alunoId = Integer.parseInt(jwt.getSubject());

        if (arquivo == null || arquivo.uploadedFile() == null) {
            throw new BusinessRuleException("O arquivo é obrigatório.");
        }

        StorageFileInput input = extrairFileInput(arquivo, "projects/" + projetoId + "/registros");
        if (input == null) {
            throw new BusinessRuleException("Falha ao processar o arquivo.");
        }

        var atualizado = gestaoProjetoUseCase.adicionarArquivoRegistro(registroId, input, alunoId);
        return Response.ok(toRegistroDiarioResponse(atualizado)).build();
    }

    @DELETE
    @Path("/{id}/registros-diarios/{registroId}/arquivos/{chave}")
    @RolesAllowed("ALUNO")
    public Response removerArquivoRegistro(
            @PathParam("id") Integer projetoId,
            @PathParam("registroId") Integer registroId,
            @PathParam("chave") String chave) {
        
        Integer alunoId = Integer.parseInt(jwt.getSubject());
        var atualizado = gestaoProjetoUseCase.removerArquivoRegistro(registroId, chave, alunoId);
        return Response.ok(toRegistroDiarioResponse(atualizado)).build();
    }

    // ==================== Helpers ====================

    private RegistroDiarioResponse toRegistroDiarioResponse(RegistroDiario registro) {
        List<RegistroDiarioArquivoResponse> arquivosResponse = registro.getArquivoChaves().stream()
                .map(chave -> new RegistroDiarioArquivoResponse(chave, storageService.gerarUrl(chave)))
                .collect(Collectors.toList());

        return new RegistroDiarioResponse(
                registro.getId(),
                registro.getTexto(),
                registro.getDataCriacao(),
                registro.getCriadoPorId(),
                registro.getProjetoId(),
                arquivosResponse
        );
    }

    private StorageFileInput extrairFileInput(FileUpload fileUpload, String prefixo) {
        if (fileUpload == null || fileUpload.uploadedFile() == null) {
            return null;
        }

        try {
            byte[] conteudo = Files.readAllBytes(fileUpload.uploadedFile());
            if (conteudo.length == 0) {
                return null;
            }

            String mimeType = fileUpload.contentType() != null
                    ? fileUpload.contentType()
                    : "application/octet-stream";

            return new StorageFileInput(
                    fileUpload.fileName(),
                    mimeType,
                    conteudo.length,
                    conteudo,
                    prefixo
            );
        } catch (IOException e) {
            throw new RuntimeException("Falha ao processar arquivo de upload.", e);
        }
    }
}
