package br.com.escola.feiraciencias.users.application.usecases;

import br.com.escola.feiraciencias.users.domain.model.Professor;
import br.com.escola.feiraciencias.users.domain.model.Aluno;

public interface CadastroUsuarioUseCase {
    Professor cadastrarProfessor(Professor professor);
    Aluno cadastrarAluno(Aluno aluno, Integer professorId);
}
