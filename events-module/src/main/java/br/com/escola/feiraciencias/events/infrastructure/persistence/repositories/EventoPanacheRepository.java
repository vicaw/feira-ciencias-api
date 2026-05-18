package br.com.escola.feiraciencias.events.infrastructure.persistence.repositories;

import br.com.escola.feiraciencias.events.domain.model.Evento;
import br.com.escola.feiraciencias.events.domain.repositories.EventoRepository;
import br.com.escola.feiraciencias.events.infrastructure.persistence.entities.EventoJpaEntity;
import br.com.escola.feiraciencias.events.infrastructure.persistence.mappers.EventoPersistenceMapper;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EventoPanacheRepository implements EventoRepository, PanacheRepositoryBase<EventoJpaEntity, Integer> {

    @Inject
    EventoPersistenceMapper mapper;

    @Override
    public Evento salvar(Evento evento) {
        EventoJpaEntity entity = mapper.toEntity(evento);
        persist(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<Evento> buscarPorId(Integer id) {
        return findByIdOptional(id).map(mapper::toDomain);
    }

    @Override
    public List<Evento> listarTodos() {
        return listAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public void excluir(Integer id) {
        deleteById(id);
        // Impl delete
    }
}
