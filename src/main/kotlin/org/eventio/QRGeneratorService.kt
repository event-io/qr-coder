package org.eventio

import io.nayuki.qrcodegen.QrCode
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import java.awt.image.BufferedImage
import java.lang.RuntimeException
import java.util.*

@ApplicationScoped
class QRGeneratorService {

    private fun toImage(qr: QrCode, scale: Int, border: Int, lightColor: Int, darkColor: Int): BufferedImage? {
        Objects.requireNonNull(qr)
        require(!(scale <= 0 || border < 0)) { "Value out of range" }
        require(!(border > Int.MAX_VALUE / 2 || qr.size + border * 2L > Int.MAX_VALUE / scale)) { "Scale or border too large" }
        val result =
            BufferedImage((qr.size + border * 2) * scale, (qr.size + border * 2) * scale, BufferedImage.TYPE_INT_RGB)
        for (y in 0 until result.height) {
            for (x in 0 until result.width) {
                val color: Boolean = qr.getModule(x / scale - border, y / scale - border)
                result.setRGB(x, y, if (color) darkColor else lightColor)
            }
        }
        return result
    }


    internal fun generateQRCodeImage(text: String, scale: Int, border: Int): Uni<BufferedImage>? {
        val qr: QrCode = QrCode.encodeBinary(text.toByteArray(), QrCode.Ecc.MEDIUM)
        return Uni.createFrom().item(toImage(qr, scale, border, 0xFFFFFF, 0x000000));
    }

    internal fun generateQRCode(text: String, scale: Int, border: Int) : Uni<ByteArray> {
        return generateQRCodeImage(text, scale, border)
            ?.map { image ->
                val baos = java.io.ByteArrayOutputStream()
                javax.imageio.ImageIO.write(image, "png", baos)
                baos.toByteArray()
            } ?: Uni.createFrom().failure(RuntimeException("Failed to generate QR code"));
    }

}