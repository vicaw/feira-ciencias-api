package br.com.escola.feiraciencias.events.api.resources;

import br.com.escola.feiraciencias.events.application.usecases.GestaoEventoUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/eventos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventoResource {

    @Inject
    GestaoEventoUseCase gestaoEventoUseCase;

    @GET
    public Response listarEventos() {
        return Response.ok(gestaoEventoUseCase.listarEventos()).build();
    }

    @POST
    public Response criarEvento(/* EventoDTO */) {
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizarEvento(@PathParam("id") Integer id /*, EventoDTO */) {
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response excluirEvento(@PathParam("id") Integer id) {
        gestaoEventoUseCase.excluirEvento(id);
        return Response.noContent().build();
    }
}
