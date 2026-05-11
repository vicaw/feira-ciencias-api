package br.com.escola.feiraciencias.events.application.usecases;

import br.com.escola.feiraciencias.events.domain.model.Evento;
import java.util.List;

public interface GestaoEventoUseCase {
    Evento criarEvento(Evento evento);
    Evento ajustarDados(Integer id, Evento eventoAtualizado);
    void excluirEvento(Integer id);
    List<Evento> listarEventos();
}
