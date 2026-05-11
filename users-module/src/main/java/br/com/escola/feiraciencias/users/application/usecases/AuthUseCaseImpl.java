package br.com.escola.feiraciencias.users.application.usecases;

import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.users.domain.model.Usuario;
import br.com.escola.feiraciencias.users.domain.repositories.UsuarioRepository;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.Duration;

@ApplicationScoped
public class AuthUseCaseImpl implements AuthUseCase {

    @Inject
    UsuarioRepository usuarioRepository;

    @Override
    public String autenticar(String email, String senha) {
        // 1. Busca o usuário
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new BusinessRuleException("Credenciais inválidas."));

        // 2. Valida a senha (aqui deveria usar bcrypt/argon2 em um cenário real)
        if (!usuario.getSenha().equals(senha)) {
            throw new BusinessRuleException("Credenciais inválidas.");
        }

        // 3. Gera o Token JWT
        return Jwt.issuer("https://feiraciencias.escola.com.br")
                .subject(usuario.getId().toString())
                .groups(usuario.getTipoUsuario().name()) // Define a Role (ALUNO, PROFESSOR)
                .claim("email", usuario.getEmail())
                .claim("nome", usuario.getNome())
                .expiresIn(Duration.ofHours(4)) // Expira em 4 horas
                .sign();
    }
}
