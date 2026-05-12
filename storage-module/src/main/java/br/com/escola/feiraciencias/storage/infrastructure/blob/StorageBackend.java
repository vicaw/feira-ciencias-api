package br.com.escola.feiraciencias.storage.infrastructure.blob;

import jakarta.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Qualificador CDI para identificar implementações de BlobStorage.
 * Uso: @StorageBackend("disk") ou @StorageBackend("database")
 */
@Qualifier
@Retention(RUNTIME)
@Target({TYPE, METHOD, FIELD, PARAMETER})
public @interface StorageBackend {
    String value();
}
