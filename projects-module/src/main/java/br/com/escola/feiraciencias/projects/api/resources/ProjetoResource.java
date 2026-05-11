package br.com.escola.feiraciencias.projects.api.resources;

import br.com.escola.feiraciencias.projects.application.usecases.GestaoProjetoUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/projetos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProjetoResource {

    @Inject
    GestaoProjetoUseCase gestaoProjetoUseCase;

    @POST
    public Response criarProjeto(/* ProjetoDTO */) {
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/{id}/integrantes")
    public Response adicionarIntegrante(@PathParam("id") Integer id /* , IntegranteDTO */) {
        return Response.ok().build();
    }

    @POST
    @Path("/{id}/comentarios")
    public Response adicionarComentario(@PathParam("id") Integer id /* , ComentarioDTO */) {
        return Response.status(Response.Status.CREATED).build();
    }
}
