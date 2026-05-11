package br.com.escola.feiraciencias.users.api.requests;

import lombok.Data;

@Data
public class AlunoRequest {
    private String nome;
    private String email;
    private String senha;
    private String matricula;
    private String anoEscolar;
}
