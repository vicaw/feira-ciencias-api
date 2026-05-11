package br.com.escola.feiraciencias.users.domain.repositories;

import br.com.escola.feiraciencias.users.domain.model.Usuario;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario salvar(Usuario usuario);
    Optional<Usuario> buscarPorId(Integer id);
    Optional<Usuario> buscarPorEmail(String email);
    // outros métodos de busca
}
