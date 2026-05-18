package br.com.escola.feiraciencias.projects.application.usecases;

import br.com.escola.feiraciencias.projects.domain.model.Comentario;
import br.com.escola.feiraciencias.projects.domain.model.Projeto;
import br.com.escola.feiraciencias.projects.domain.model.ProjetoUsuario;
import br.com.escola.feiraciencias.projects.domain.model.RegistroDiario;
import br.com.escola.feiraciencias.projects.domain.repositories.ComentarioRepository;
import br.com.escola.feiraciencias.projects.domain.repositories.ProjetoRepository;
import br.com.escola.feiraciencias.projects.domain.repositories.ProjetoUsuarioRepository;
import br.com.escola.feiraciencias.projects.domain.repositories.RegistroDiarioRepository;
import br.com.escola.feiraciencias.shared.domain.enums.SituacaoProjeto;
import br.com.escola.feiraciencias.shared.domain.enums.TipoIntegrante;
import br.com.escola.feiraciencias.shared.domain.enums.TipoUsuario;
import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.shared.domain.exceptions.EntityNotFoundException;
import br.com.escola.feiraciencias.users.application.services.UsuarioService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.persistence.criteria.CriteriaBuilder.In;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class GestaoProjetoUseCase {

    @Inject
    ProjetoRepository projetoRepository;

    @Inject
    ComentarioRepository comentarioRepository;

    @Inject
    RegistroDiarioRepository registroDiarioRepository;

    @Inject
    ProjetoUsuarioRepository projetoUsuarioRepository;

    @Inject
    UsuarioService usuarioService;

    // ==================== PROJETOS ====================

    @Transactional
    public Projeto criarProjeto(Projeto projeto, Integer professorId) {
        var usuario = usuarioService.buscarUsuarioPorId(professorId);
        if (!usuario.getTipoUsuario().equals(TipoUsuario.PROFESSOR)) {
            throw new BusinessRuleException("Apenas professores podem criar projetos.");
        }

        projeto.setDataCriacao(java.time.LocalDate.now());
        projeto.setCriadoPorId(professorId);
        projeto.setSituacao(SituacaoProjeto.ATIVO);

        return projetoRepository.salvar(projeto);
    }

    public Projeto buscarProjetoPorId (Integer id) {
        return projetoRepository.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado."));
    }

    public List<Projeto> listarProjetosPorEvento(Integer eventoId) {
        return projetoRepository.listarPorEvento(eventoId);
    }

    @Transactional
    public Projeto atualizarProjeto(Integer id, Projeto projetoAtualizado, Integer professorId) {
        Projeto projeto = buscarProjetoPorId(id);

        if (!projeto.getCriadoPorId().equals(professorId)) {
            throw new BusinessRuleException("Apenas o criador do projeto pode atualizá-lo.");
        }

        projeto.setTitulo(projetoAtualizado.getTitulo());
        projeto.setDescricao(projetoAtualizado.getDescricao());
        projeto.setMateriais(projetoAtualizado.getMateriais());
        projeto.setAreaDeConhecimento(projetoAtualizado.getAreaDeConhecimento());
        projeto.setSerie(projetoAtualizado.getSerie());
        projeto.setDataApresentacao(projetoAtualizado.getDataApresentacao());
        projeto.setSituacao(projetoAtualizado.getSituacao());

        return projetoRepository.salvar(projeto);
    }

    @Transactional
    public Projeto atualizarMateriaisDescricao(Integer id, String descricao, String materiais, Integer usuarioId) {
        Projeto projeto = buscarProjetoPorId(id);
        var usuario = usuarioService.buscarUsuarioPorId(usuarioId);

        // Alunos podem editar materiais e descrição
        if (usuario.getTipoUsuario().equals(TipoUsuario.ALUNO)) {
            projeto.setDescricao(descricao);
            projeto.setMateriais(materiais);
            return projetoRepository.salvar(projeto);
        }

        // Professores também podem
        if (usuario.getTipoUsuario().equals(TipoUsuario.PROFESSOR)) {
            projeto.setDescricao(descricao);
            projeto.setMateriais(materiais);
            return projetoRepository.salvar(projeto);
        }

        throw new BusinessRuleException("Apenas alunos e professores podem atualizar materiais e descrição.");
    }

    @Transactional
    public void excluirProjeto(Integer id, Integer professorId) {
        Projeto projeto = buscarProjetoPorId(id);

        if (!projeto.getCriadoPorId().equals(professorId)) {
            throw new BusinessRuleException("Apenas o criador do projeto pode deletá-lo.");
        }

        projetoRepository.excluir(id);
    }

    // ==================== INTEGRANTES ====================

    @Transactional
    public ProjetoUsuario adicionarIntegrante(Integer projetoId, Integer usuarioId, String tipoIntegrante, Integer professorId) {
        Projeto projeto = buscarProjetoPorId(projetoId);

        if (!projeto.getCriadoPorId().equals(professorId)) {
            throw new BusinessRuleException("Apenas o professor criador pode adicionar integrantes.");
        }

        // Validar usuário
        usuarioService.buscarUsuarioPorId(usuarioId);

        ProjetoUsuario integrante = new ProjetoUsuario();
        integrante.setProjetoId(projetoId);
        integrante.setUsuarioId(usuarioId);
        integrante.setTipoIntegrante(TipoIntegrante.valueOf(tipoIntegrante));
        integrante.setDataVinculo(LocalDateTime.now());

        return projetoUsuarioRepository.salvar(integrante);
    }

    public List<ProjetoUsuario> listarIntegrantes(Integer projetoId) {
        buscarProjetoPorId(projetoId);
        return projetoUsuarioRepository.listarPorProjeto(projetoId);
    }

    // ==================== COMENTÁRIOS ====================

    @Transactional
    public Comentario adicionarComentario(Integer projetoId, Comentario comentario, Integer usuarioId) {
        Projeto projeto = buscarProjetoPorId(projetoId);
        usuarioService.buscarUsuarioPorId(usuarioId);

        comentario.setProjetoId(projetoId);
        comentario.setCriadoPorId(usuarioId);
        comentario.setDataComentario(LocalDateTime.now());

        return comentarioRepository.salvar(comentario);
    }

    public List<Comentario> listarComentarios(Integer projetoId) {
        buscarProjetoPorId(projetoId);
        return comentarioRepository.listarPorProjeto(projetoId);
    }

    // ==================== REGISTROS DIÁRIOS ====================

    @Transactional
    public RegistroDiario criarRegistroDiario(Integer projetoId, RegistroDiario registro, Integer alunoId) {
        Projeto projeto = buscarProjetoPorId(projetoId);
        var usuario = usuarioService.buscarUsuarioPorId(alunoId);

        if (!usuario.getTipoUsuario().equals(TipoUsuario.ALUNO)) {
            throw new BusinessRuleException("Apenas alunos podem criar registros diários.");
        }

        registro.setProjetoId(projetoId);
        registro.setCriadoPorId(alunoId);
        registro.setDataCriacao(LocalDateTime.now());

        return registroDiarioRepository.salvar(registro);
    }

    public List<RegistroDiario> listarRegistrosDiarios(Integer projetoId) {
        buscarProjetoPorId(projetoId);
        return registroDiarioRepository.listarPorProjeto(projetoId);
    }
}
