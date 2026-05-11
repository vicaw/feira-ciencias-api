package br.com.escola.feiraciencias.users.application.usecases;

import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.users.domain.model.Usuario;
import br.com.escola.feiraciencias.users.domain.repositories.UsuarioRepository;
import br.com.escola.feiraciencias.users.domain.services.PasswordService;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.Duration;

@ApplicationScoped
public class AuthUseCase {

    @Inject
    br.com.escola.feiraciencias.users.application.services.UsuarioService usuarioService;

    @Inject
    PasswordService passwordService;

    public String execute(String email, String senha) {
        Usuario usuario = usuarioService.buscarPorEmailOuFalhar(email, "Credenciais inválidas.");

        if (!passwordService.verify(senha, usuario.getSenha())) {
            throw new BusinessRuleException("Credenciais inválidas.");
        }

        return Jwt.issuer("https://feiraciencias.escola.com.br")
                .subject(usuario.getId().toString())
                .groups(usuario.getTipoUsuario().name())
                .claim("email", usuario.getEmail())
                .claim("nome", usuario.getNome())
                .expiresIn(Duration.ofHours(4))
                .sign();
    }
}
