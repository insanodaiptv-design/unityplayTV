package tv.own.owntv.core.network
import java.io.InputStream
class HttpClient(var response: (String) -> String) {
    class HttpStatusException(val code: Int): RuntimeException("HTTP $code")
    suspend fun <T> get(url: String, userAgent: String? = null,
        onProgress: ((Long, Long?) -> Unit)? = null, maxAttempts: Int = 1,
        block: suspend (InputStream) -> T): T = response(url).byteInputStream().use { block(it) }
}
