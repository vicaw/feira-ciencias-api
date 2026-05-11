package br.com.escola.feiraciencias.projects.infrastructure.persistence.repositories;

import br.com.escola.feiraciencias.projects.domain.model.Projeto;
import br.com.escola.feiraciencias.projects.domain.repositories.ProjetoRepository;
import br.com.escola.feiraciencias.projects.infrastructure.persistence.entities.ProjetoJpaEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProjetoPanacheRepository implements ProjetoRepository, PanacheRepositoryBase<ProjetoJpaEntity, Integer> {

    @Override
    public Projeto salvar(Projeto projeto) {
        return null;
    }

    @Override
    public Optional<Projeto> buscarPorId(Integer id) {
        return Optional.empty();
    }

    @Override
    public List<Projeto> listarPorEvento(Integer eventoId) {
        return List.of();
    }
}
