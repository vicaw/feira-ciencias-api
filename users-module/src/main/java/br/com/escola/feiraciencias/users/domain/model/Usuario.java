package br.com.escola.feiraciencias.users.domain.model;

import br.com.escola.feiraciencias.shared.domain.enums.TipoUsuario;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Usuario {
    private Integer id;
    private String nome;
    private String email;
    private String senha;
    private TipoUsuario tipoUsuario;
    private LocalDateTime dataCadastro;
    private Integer criadoPorId;
}
