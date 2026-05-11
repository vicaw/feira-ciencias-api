package br.com.escola.feiraciencias.users.api.resources;

import br.com.escola.feiraciencias.users.api.requests.LoginRequest;
import br.com.escola.feiraciencias.users.api.responses.TokenResponse;
import br.com.escola.feiraciencias.users.application.usecases.AuthUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthUseCase authUseCase;

    @POST
    @Path("/login")
    public Response login(LoginRequest loginRequest) {
        String token = authUseCase.autenticar(loginRequest.getEmail(), loginRequest.getSenha());
        return Response.ok(new TokenResponse(token)).build();
    }
}
