package br.com.escola.feiraciencias.users.api.resources;

import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.users.api.mappers.UsuarioApiMapper;
import br.com.escola.feiraciencias.users.api.requests.AlunoRequest;
import br.com.escola.feiraciencias.users.application.usecases.CadastroUsuarioUseCase;
import br.com.escola.feiraciencias.users.domain.model.Aluno;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    CadastroUsuarioUseCase cadastroUsuarioUseCase;

    @Inject
    JsonWebToken jwt;

    @Inject
    UsuarioApiMapper apiMapper;

    @POST
    @Path("/professores")
    public Response cadastrarProfessor(/* ProfessorRequest request */) {
        // Lógica de delegação para o UseCase
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/alunos")
    public Response cadastrarAluno(AlunoRequest request) {
        String sub = jwt.getSubject();
        if (sub == null) {
            throw new BusinessRuleException("Token JWT ausente ou inválido.");
        }
        Integer professorId = Integer.parseInt(sub);

        Aluno aluno = apiMapper.toDomain(request);

        Aluno cadastrado = cadastroUsuarioUseCase.cadastrarAluno(aluno, professorId);

        return Response.status(Response.Status.CREATED).entity(cadastrado).build();
    }
}
