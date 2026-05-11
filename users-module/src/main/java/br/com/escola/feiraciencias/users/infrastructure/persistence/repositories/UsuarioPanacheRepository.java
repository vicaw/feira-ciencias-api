package br.com.escola.feiraciencias.users.infrastructure.persistence.repositories;

import br.com.escola.feiraciencias.users.domain.model.Aluno;
import br.com.escola.feiraciencias.users.domain.model.Professor;
import br.com.escola.feiraciencias.users.domain.model.Usuario;
import br.com.escola.feiraciencias.users.domain.repositories.UsuarioRepository;
import br.com.escola.feiraciencias.users.infrastructure.persistence.entities.AlunoJpaEntity;
import br.com.escola.feiraciencias.users.infrastructure.persistence.entities.ProfessorJpaEntity;
import br.com.escola.feiraciencias.users.infrastructure.persistence.entities.UsuarioJpaEntity;
import br.com.escola.feiraciencias.users.infrastructure.persistence.mappers.UsuarioPersistenceMapper;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class UsuarioPanacheRepository implements UsuarioRepository, PanacheRepositoryBase<UsuarioJpaEntity, Integer> {

    @Inject
    UsuarioPersistenceMapper persistenceMapper;

    @Override
    public Usuario salvar(Usuario usuario) {
        UsuarioJpaEntity entity = persistenceMapper.toEntity(usuario);
        persist(entity);
        return persistenceMapper.toDomain(entity);
    }

    @Override
    public Optional<Usuario> buscarPorId(Integer id) {
        return findByIdOptional(id).map(persistenceMapper::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return find("email", email).firstResultOptional().map(persistenceMapper::toDomain);
    }
}
