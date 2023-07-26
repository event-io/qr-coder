package org.eventio

data class QRRequest(
    val text: String,
    val scale: Int = 5,
    val border: Int = 2,
    val meta: Map<String, String> = emptyMap(),
)