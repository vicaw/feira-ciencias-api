package br.com.escola.feiraciencias.storage.infrastructure.persistence.repositories;

import br.com.escola.feiraciencias.storage.infrastructure.persistence.entities.ArquivoJpaEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ArquivoPanacheRepository implements PanacheRepositoryBase<ArquivoJpaEntity, String> {
    // Panache fornece: persist, findById, listAll, delete, count, etc.
}
