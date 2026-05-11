package br.com.escola.feiraciencias.events.domain.repositories;

import br.com.escola.feiraciencias.events.domain.model.Evento;
import java.util.Optional;
import java.util.List;

public interface EventoRepository {
    Evento salvar(Evento evento);
    Optional<Evento> buscarPorId(Integer id);
    List<Evento> listarTodos();
    void excluir(Integer id);
}
