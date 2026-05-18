package br.com.escola.feiraciencias.events.api.resources;

import br.com.escola.feiraciencias.events.api.dto.requests.CriarEventoRequest;
import br.com.escola.feiraciencias.events.api.dto.requests.AtualizarEventoRequest;
import br.com.escola.feiraciencias.events.api.dto.responses.EventoResponse;
import br.com.escola.feiraciencias.events.api.mappers.EventoApiMapper;
import br.com.escola.feiraciencias.events.application.usecases.GestaoEventoUseCase;
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

@Path("/eventos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventoResource {

    @Inject
    GestaoEventoUseCase gestaoEventoUseCase;

    @Inject
    EventoApiMapper mapper;

    @Inject
    JsonWebToken jwt;

    @GET
    public Response listarEventos() {
        List<EventoResponse> eventos = gestaoEventoUseCase.listarEventos().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return Response.ok(eventos).build();
    }

    @GET
    @Path("/{id}")
    public Response obterEvento(@PathParam("id") Integer id) {
        var evento = gestaoEventoUseCase.buscarEventoPorId(id);
        return Response.ok(mapper.toResponse(evento)).build();
    }

    @POST
    @RolesAllowed("PROFESSOR")
    public Response criarEvento(@Valid CriarEventoRequest request) {
        Integer professorId = Integer.parseInt(jwt.getSubject());
        
        // Verificar se é professor admin
        boolean isAdmin = gestaoEventoUseCase.isProfessorAdmin(professorId);
        if (!isAdmin) {
            throw new BusinessRuleException("Apenas professores administradores podem criar eventos.");
        }

        var evento = mapper.toDomain(request);
        var criado = gestaoEventoUseCase.criarEvento(evento, professorId);
        return Response.status(Response.Status.CREATED).entity(mapper.toResponse(criado)).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("PROFESSOR")
    public Response atualizarEvento(@PathParam("id") Integer id, @Valid AtualizarEventoRequest request) {
        Integer professorId = Integer.parseInt(jwt.getSubject());
        
        var evento = mapper.toDomain(request);
        var atualizado = gestaoEventoUseCase.atualizarEvento(id, evento, professorId);
        return Response.ok(mapper.toResponse(atualizado)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("PROFESSOR")
    public Response excluirEvento(@PathParam("id") Integer id) {
        Integer professorId = Integer.parseInt(jwt.getSubject());
        gestaoEventoUseCase.excluirEvento(id, professorId);
        return Response.noContent().build();
    }
}
