package br.com.escola.feiraciencias.projects.application.usecases;

import br.com.escola.feiraciencias.projects.domain.model.Comentario;
import br.com.escola.feiraciencias.projects.domain.model.Projeto;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GestaoProjetoUseCase {

    public Projeto criarProjeto(Projeto projeto) {
        return projeto;
    }

    public Projeto adicionarIntegrante(Integer projetoId, Integer usuarioId, String tipoIntegrante) {
        return null;
    }

    public Comentario adicionarComentario(Integer projetoId, Comentario comentario) {
        return comentario;
    }

    public Projeto atualizarMateriais(Integer projetoId, String materiais) {
        return null;
    }

    public void fecharProjeto(Integer projetoId) {
        // Implementação em breve
    }
}
