import br.com.escola.feiraciencias.users.api.dto.requests.LoginRequest;
import br.com.escola.feiraciencias.users.api.dto.responses.LoginResponse;
import br.com.escola.feiraciencias.users.application.usecases.AuthUseCase;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
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
    public Response login(@Valid LoginRequest loginRequest) {
        String token = authUseCase.execute(loginRequest.email(), loginRequest.senha());
        return Response.ok(new LoginResponse(token)).build();
    }
}
