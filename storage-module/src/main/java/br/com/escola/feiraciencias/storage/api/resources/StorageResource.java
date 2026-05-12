package br.com.escola.feiraciencias.storage.api.resources;

import br.com.escola.feiraciencias.storage.application.dto.ArquivoInfo;
import br.com.escola.feiraciencias.storage.application.dto.ArquivoUpload;
import br.com.escola.feiraciencias.storage.application.usecases.StorageUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.IOException;
import java.nio.file.Files;

/**
 * Endpoint de armazenamento de arquivos.
 *
 * Upload: POST /arquivos        (multipart/form-data)
 * Metadados: GET /arquivos/{id} (retorna metadados sem conteúdo binário)
 * Download:  GET /arquivos/{id}/conteudo (retorna base64 do arquivo)
 */
@Path("/arquivos")
@Produces(MediaType.APPLICATION_JSON)
public class StorageResource {

    @Inject
    StorageUseCase storageUseCase;

    /**
     * Recebe um arquivo via multipart/form-data (padrão da indústria).
     * Não aceita base64 no body — isso aumentaria o payload em ~33%.
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(
            @RestForm("arquivo") 
            @Schema(type = SchemaType.STRING, format = "binary", description = "Arquivo binário a ser enviado")
            FileUpload arquivo,
            @RestForm("criadoPorId") Integer criadoPorId) {

        if (arquivo == null || arquivo.uploadedFile() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"erro\": \"Campo 'arquivo' é obrigatório.\"}")
                    .build();
        }

        try {
            byte[] conteudo = Files.readAllBytes(arquivo.uploadedFile());
            String mimeType = arquivo.contentType() != null
                    ? arquivo.contentType()
                    : "application/octet-stream";

            ArquivoUpload upload = new ArquivoUpload(
                    arquivo.fileName(),
                    mimeType,
                    conteudo.length,
                    conteudo,
                    criadoPorId
            );

            ArquivoInfo info = storageUseCase.salvarArquivo(upload);
            return Response.status(Response.Status.CREATED).entity(info).build();

        } catch (IOException e) {
            return Response.serverError()
                    .entity("{\"erro\": \"Falha ao processar o arquivo.\"}")
                    .build();
        }
    }

    /**
     * Retorna apenas os metadados do arquivo (sem o conteúdo binário).
     */
    @GET
    @Path("/{id}")
    public Response metadados(@PathParam("id") String id) {
        ArquivoInfo info = storageUseCase.recuperarMetadados(id);
        return Response.ok(info).build();
    }

    /**
     * Retorna o conteúdo do arquivo em Base64.
     * Útil para exibição inline (ex: imagens) no front-end.
     */
    @GET
    @Path("/{id}/conteudo")
    public Response conteudo(@PathParam("id") String id) {
        String base64 = storageUseCase.recuperarBase64(id);
        return Response.ok("{\"conteudo\": \"" + base64 + "\"}").build();
    }
}
