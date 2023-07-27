package org.eventio

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.BucketItem
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import io.quarkus.runtime.StartupEvent
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.asUni
import jakarta.enterprise.event.Observes
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
@Singleton
class SupabaseClientService {

    private lateinit var client: SupabaseClient

    @Inject
    lateinit var supabase: Supabase

    fun onStartup(@Observes event: StartupEvent?) {
        client = createSupabaseClient(
            supabase.url(),
            supabase.key(),
        ) {
            install(Storage)
        }
    }

    fun uploadQRCode(qrCode: ByteArray, directory: String, name: String): Uni<String> {
        return GlobalScope.async {
            client.storage[supabase.bucket()]
            .upload("codes/${directory}/${name}.png", qrCode)
        }.asUni()
    }

    fun getQRCodes(directory: String): Uni<List<BucketItem>> {
        return GlobalScope.async {
            client.storage[supabase.bucket()]
                .list("codes/${directory}")
        }.asUni()
    }

}