package br.com.escola.feiraciencias.projects.application.usecases;

import br.com.escola.feiraciencias.projects.domain.model.Projeto;
import br.com.escola.feiraciencias.projects.domain.model.Comentario;

public interface GestaoProjetoUseCase {
    Projeto criarProjeto(Projeto projeto);
    Projeto adicionarIntegrante(Integer projetoId, Integer usuarioId, String tipoIntegrante);
    Comentario adicionarComentario(Integer projetoId, Comentario comentario);
    Projeto atualizarMateriais(Integer projetoId, String materiais);
    void fecharProjeto(Integer projetoId);
}
