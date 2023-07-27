package org.eventio

import io.smallrye.config.ConfigMapping

@ConfigMapping(prefix = "qr-generator")
interface QRGenerator {
    fun border(): Int
    fun scale(): Int
}