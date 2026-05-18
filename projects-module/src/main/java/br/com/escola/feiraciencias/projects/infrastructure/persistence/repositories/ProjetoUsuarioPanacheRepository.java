package br.com.escola.feiraciencias.projects.infrastructure.persistence.repositories;

import br.com.escola.feiraciencias.projects.domain.model.ProjetoUsuario;
import br.com.escola.feiraciencias.projects.domain.repositories.ProjetoUsuarioRepository;
import br.com.escola.feiraciencias.projects.infrastructure.persistence.entities.ProjetoUsuarioJpaEntity;
import br.com.escola.feiraciencias.projects.infrastructure.persistence.mappers.ProjetoUsuarioPersistenceMapper;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProjetoUsuarioPanacheRepository implements ProjetoUsuarioRepository, PanacheRepositoryBase<ProjetoUsuarioJpaEntity, Integer> {

    @Inject
    ProjetoUsuarioPersistenceMapper mapper;

    @Override
    public ProjetoUsuario salvar(ProjetoUsuario projetoUsuario) {
        ProjetoUsuarioJpaEntity entity = mapper.toEntity(projetoUsuario);
        persist(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public List<ProjetoUsuario> listarPorProjeto(Integer projetoId) {
        return list("projetoId", projetoId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
