package br.com.escola.feiraciencias.projects.domain.repositories;

import br.com.escola.feiraciencias.projects.domain.model.Comentario;
import java.util.List;

public interface ComentarioRepository {
    Comentario salvar(Comentario comentario);
    List<Comentario> listarPorProjeto(Integer projetoId);
}
