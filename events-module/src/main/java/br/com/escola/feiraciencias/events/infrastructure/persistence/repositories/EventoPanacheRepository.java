package br.com.escola.feiraciencias.events.infrastructure.persistence.repositories;

import br.com.escola.feiraciencias.events.domain.model.Evento;
import br.com.escola.feiraciencias.events.domain.repositories.EventoRepository;
import br.com.escola.feiraciencias.events.infrastructure.persistence.entities.EventoJpaEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.List;

@ApplicationScoped
public class EventoPanacheRepository implements EventoRepository, PanacheRepositoryBase<EventoJpaEntity, Integer> {

    @Override
    public Evento salvar(Evento evento) {
        return null; // Impl mapping
    }

    @Override
    public Optional<Evento> buscarPorId(Integer id) {
        return Optional.empty();
    }

    @Override
    public List<Evento> listarTodos() {
        return List.of();
    }

    @Override
    public void excluir(Integer id) {
        // Impl delete
    }
}
