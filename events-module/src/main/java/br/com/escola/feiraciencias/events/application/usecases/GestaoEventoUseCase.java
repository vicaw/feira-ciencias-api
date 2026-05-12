package br.com.escola.feiraciencias.events.application.usecases;

import br.com.escola.feiraciencias.events.domain.model.Evento;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class GestaoEventoUseCase {

    public Evento criarEvento(Evento evento) {
        return evento;
    }

    public Evento ajustarDados(Integer id, Evento eventoAtualizado) {
        return eventoAtualizado;
    }

    public void excluirEvento(Integer id) {
        // Implementação em breve
    }

    public List<Evento> listarEventos() {
        return Collections.emptyList();
    }
}
