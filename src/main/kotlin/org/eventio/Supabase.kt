package org.eventio

import io.smallrye.config.ConfigMapping

@ConfigMapping(prefix = "supabase")
interface Supabase {
    fun url(): String
    fun key(): String
    fun bucket(): String
}