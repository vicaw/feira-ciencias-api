package br.com.escola.feiraciencias.projects.domain.repositories;

import br.com.escola.feiraciencias.projects.domain.model.RegistroDiario;
import java.util.List;

public interface RegistroDiarioRepository {
    RegistroDiario salvar(RegistroDiario registro);
    List<RegistroDiario> listarPorProjeto(Integer projetoId);
}
