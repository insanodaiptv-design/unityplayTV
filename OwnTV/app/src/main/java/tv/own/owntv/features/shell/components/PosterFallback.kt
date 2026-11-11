package tv.own.owntv.features.shell.components

import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

/** Provider URLs remain preferred; reject empty/sentinel values before asking the image loader. */
internal object PosterUrlPolicy {
    fun usable(raw: String?): String? {
        val value = raw?.trim()?.takeIf { it.isNotEmpty() } ?: return null
        val url = if (value.startsWith("//")) "https:$value" else value
        return url.takeIf { it.startsWith("https://", true) || it.startsWith("http://", true) }
    }
}

/** A visible missing/broken poster may resolve metadata, with at most two lookups across both catalogs. */
internal object PosterFallbackLoader {
    private val permits = Semaphore(2)
    suspend fun load(resolve: suspend () -> String?): String? = permits.withPermit { resolve() }
}
