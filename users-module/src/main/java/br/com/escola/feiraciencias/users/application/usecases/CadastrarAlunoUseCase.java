package br.com.escola.feiraciencias.users.application.usecases;

import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.users.application.services.UsuarioService;
import br.com.escola.feiraciencias.users.domain.model.Aluno;
import br.com.escola.feiraciencias.users.domain.model.Professor;
import br.com.escola.feiraciencias.users.domain.model.Usuario;
import br.com.escola.feiraciencias.users.domain.repositories.UsuarioRepository;
import br.com.escola.feiraciencias.users.domain.services.PasswordService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CadastrarAlunoUseCase {

    @Inject
    UsuarioService usuarioService;

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    PasswordService passwordService;

    @Transactional
    public Aluno execute(Aluno aluno, Integer professorId) {
        Usuario orientador = usuarioService.buscarPorIdOuFalhar(professorId);

        if (!orientador.isProfessor()) {
            throw new BusinessRuleException("Apenas professores podem cadastrar alunos.");
        }

        usuarioService.verificarEmailDisponivel(aluno.getEmail());

        aluno.definirDadosEscolares(aluno.getMatricula(), aluno.getAnoEscolar());
        aluno.registrar(passwordService.hash(aluno.getSenha()));
        aluno.vincularAoProfessor(professorId);

        return (Aluno) usuarioRepository.salvar(aluno);
    }
}
