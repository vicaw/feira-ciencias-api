package br.com.escola.feiraciencias.users.application.usecases;

import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.users.application.services.UsuarioService;
import br.com.escola.feiraciencias.users.domain.model.Professor;
import br.com.escola.feiraciencias.users.domain.repositories.UsuarioRepository;
import br.com.escola.feiraciencias.users.domain.services.PasswordService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CadastrarProfessorUseCase {

    @Inject
    UsuarioService usuarioService;

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    PasswordService passwordService;

    @Transactional
    public Professor execute(Professor professor) {
        usuarioService.verificarEmailDisponivel(professor.getEmail());
        professor.registrar(passwordService.hash(professor.getSenha()));
        return (Professor) usuarioRepository.salvar(professor);
    }
}
