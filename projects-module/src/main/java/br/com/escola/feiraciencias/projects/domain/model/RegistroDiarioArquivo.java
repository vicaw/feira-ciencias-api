package br.com.escola.feiraciencias.projects.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroDiarioArquivo {
    private Integer id;
    private String base64;
    private Integer registroDiarioId;

    // Getters and Setters omitted
}
