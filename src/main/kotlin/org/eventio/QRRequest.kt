package org.eventio

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class QRRequest(
    val text: String,
    val scale: Int?,
    val border: Int?,
    val meta: JsonObject?,
)

fun JsonObject?.nget(key: String, default: Any): Any {
    return this?.getOrDefault(key, default) ?: default
}