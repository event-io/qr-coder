package org.eventio

import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import java.util.*


@Path("/qr")
class QRResource {

    @Inject
    lateinit var qrGeneratorService: QRGeneratorService

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces("image/png")
    fun qr(
        @QueryParam("text") text: String,
        @DefaultValue("5") @QueryParam("scale") scale: Int = 5,
        @DefaultValue("2") @QueryParam("border") border: Int = 2,
        @DefaultValue("") @QueryParam("meta") meta: String = "",
    ): Uni<ByteArray> {
        return qrGeneratorService.generateQRCode(text, scale, border);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("image/png")
    fun qr(
        qrRequest: QRRequest,
    ): Uni<ByteArray> {
        return qrGeneratorService.generateQRCode(qrRequest.text, qrRequest.scale, qrRequest.border);
    }
}