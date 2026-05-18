package br.com.escola.feiraciencias.events.application.usecases;

import br.com.escola.feiraciencias.events.domain.model.Evento;
import br.com.escola.feiraciencias.events.domain.repositories.EventoRepository;
import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.shared.domain.exceptions.EntityNotFoundException;
import br.com.escola.feiraciencias.shared.domain.enums.SituacaoEvento;
import br.com.escola.feiraciencias.users.application.services.UsuarioService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class GestaoEventoUseCase {

    @Inject
    EventoRepository eventoRepository;

    @Inject
    UsuarioService usuarioService;

    @Transactional
    public Evento criarEvento(Evento evento, Integer professorId) {
        validarProfessorAdmin(professorId);
        evento.setDataCriacao(LocalDateTime.now());
        evento.setCriadoPorId(professorId);
        evento.setSituacao(SituacaoEvento.ATIVO);
        return eventoRepository.salvar(evento);
    }

    public Evento buscarEventoPorId(Integer id) {
        return eventoRepository.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado."));
    }

    @Transactional
    public Evento atualizarEvento(Integer id, Evento eventoAtualizado, Integer professorId) {
        Evento evento = buscarEventoPorId(id);
        
        // Apenas o criador ou admin pode atualizar
        if (!evento.getCriadoPorId().equals(professorId)) {
            validarProfessorAdmin(professorId);
        }

        evento.setNome(eventoAtualizado.getNome());
        evento.setDescricao(eventoAtualizado.getDescricao());
        evento.setDataInicio(eventoAtualizado.getDataInicio());
        evento.setDataFim(eventoAtualizado.getDataFim());
        evento.setSituacao(eventoAtualizado.getSituacao());

        return eventoRepository.salvar(evento);
    }

    @Transactional
    public void excluirEvento(Integer id, Integer professorId) {
        Evento evento = buscarEventoPorId(id);
        
        // Apenas o criador ou admin pode deletar
        if (!evento.getCriadoPorId().equals(professorId)) {
            validarProfessorAdmin(professorId);
        }

        eventoRepository.excluir(id);
    }

    public List<Evento> listarEventos() {
        return eventoRepository.listarTodos();
    }

    public boolean isProfessorAdmin(Integer professorId) {
        try {
            var usuario = usuarioService.buscarUsuarioPorId(professorId);
            return usuario.getTipoUsuario().equals(br.com.escola.feiraciencias.shared.domain.enums.TipoUsuario.PROFESSOR) 
                    && usuario instanceof br.com.escola.feiraciencias.users.domain.model.Professor
                    && ((br.com.escola.feiraciencias.users.domain.model.Professor) usuario).getIsAdm();
        } catch (Exception e) {
            return false;
        }
    }

    private void validarProfessorAdmin(Integer professorId) {
        if (!isProfessorAdmin(professorId)) {
            throw new BusinessRuleException("Apenas professores administradores podem realizar esta ação.");
        }
    }
}
