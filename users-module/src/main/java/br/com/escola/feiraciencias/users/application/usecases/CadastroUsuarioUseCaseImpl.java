package br.com.escola.feiraciencias.users.application.usecases;

import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.users.domain.model.Aluno;
import br.com.escola.feiraciencias.users.domain.model.Professor;
import br.com.escola.feiraciencias.users.domain.model.Usuario;
import br.com.escola.feiraciencias.users.domain.repositories.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

@ApplicationScoped
public class CadastroUsuarioUseCaseImpl implements CadastroUsuarioUseCase {

    @Inject
    UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public Professor cadastrarProfessor(Professor professor) {
        if (usuarioRepository.buscarPorEmail(professor.getEmail()).isPresent()) {
            throw new BusinessRuleException("Email já cadastrado.");
        }
        professor.setDataCadastro(LocalDateTime.now());
        return (Professor) usuarioRepository.salvar(professor);
    }

    @Override
    @Transactional
    public Aluno cadastrarAluno(Aluno aluno, Integer professorId) {
        // Verifica se professorId existe e é professor
        Usuario prof = usuarioRepository.buscarPorId(professorId)
                .orElseThrow(() -> new BusinessRuleException("Professor não encontrado."));
        
        if (!(prof instanceof Professor)) {
            throw new BusinessRuleException("Apenas professores podem cadastrar alunos.");
        }

        if (usuarioRepository.buscarPorEmail(aluno.getEmail()).isPresent()) {
            throw new BusinessRuleException("Email já cadastrado.");
        }

        aluno.setCriadoPorId(professorId);
        aluno.setDataCadastro(LocalDateTime.now());
        
        return (Aluno) usuarioRepository.salvar(aluno);
    }
}
