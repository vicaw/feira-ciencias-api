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
        // Implementação básica usando o mapper
        if (usuario instanceof Aluno aluno) {
            AlunoJpaEntity entity = persistenceMapper.toEntity(aluno);
            persist(entity);
            return persistenceMapper.toDomain(entity);
        } else if (usuario instanceof Professor prof) {
            ProfessorJpaEntity entity = persistenceMapper.toEntity(prof);
            persist(entity);
            return persistenceMapper.toDomain(entity);
        }
        throw new IllegalArgumentException("Unknown domain type");
    }

    @Override
    public Optional<Usuario> buscarPorId(Integer id) {
        return findByIdOptional(id).map(persistenceMapper::toDomainBase);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return find("email", email).firstResultOptional().map(persistenceMapper::toDomainBase);
    }
}
