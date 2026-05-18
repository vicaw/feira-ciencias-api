package br.com.escola.feiraciencias.projects.domain.repositories;

import br.com.escola.feiraciencias.projects.domain.model.ProjetoUsuario;
import java.util.List;

public interface ProjetoUsuarioRepository {
    ProjetoUsuario salvar(ProjetoUsuario projetoUsuario);
    List<ProjetoUsuario> listarPorProjeto(Integer projetoId);
}
