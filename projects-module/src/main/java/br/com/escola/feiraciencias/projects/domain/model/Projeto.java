package br.com.escola.feiraciencias.projects.domain.model;

import br.com.escola.feiraciencias.shared.domain.enums.SituacaoProjeto;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Projeto {
    private Integer id;
    private String titulo;
    private String descricao;
    private String materiais; // Pode ser mapeado como entidade separada se houver controle de estoque, mas no MER está como texto. O caso de uso pede lista de materiais. Vamos deixar como texto ou lista.
    private LocalDate dataCriacao;
    private LocalDate dataApresentacao;
    private SituacaoProjeto situacao;
    private String areaDeConhecimento;
    private String serie;
    private Integer criadoPorId;
    private Integer eventoId;
    private String imagemCapaChave;
}
