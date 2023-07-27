package org.eventio

import io.github.jan.supabase.storage.BucketItem
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import java.util.*


@Path("/qr")
class QRResource {

    @Inject
    lateinit var qrGeneratorService: QRGeneratorService

    @Inject
    lateinit var supabaseClientService: SupabaseClientService

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("image/png")
    fun generate(
        qrRequest: QRRequest,
    ): Uni<ByteArray> {
        return qrGeneratorService
            .generateQRCode(qrRequest.text, qrRequest.scale, qrRequest.border)
            .onItem().call { byteArray ->
                supabaseClientService.uploadQRCode(
                    byteArray,
                    qrRequest.meta.nget("directory", UUID.randomUUID()).toString(),
                    qrRequest.meta.nget("name", UUID.randomUUID()).toString()
                )
            }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun list(
        @QueryParam("directory") directory: String,
    ): Uni<List<BucketItem>> {
        return supabaseClientService.getQRCodes(directory)
    }
}