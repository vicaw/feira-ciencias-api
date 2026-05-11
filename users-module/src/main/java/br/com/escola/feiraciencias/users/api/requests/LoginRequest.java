package br.com.escola.feiraciencias.users.api.requests;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String senha;
}
