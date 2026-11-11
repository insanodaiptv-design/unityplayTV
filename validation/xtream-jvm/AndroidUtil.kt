package android.util
typealias JsonReader = com.google.gson.stream.JsonReader
typealias JsonToken = com.google.gson.stream.JsonToken
object Base64 {
    const val DEFAULT = 0
    fun decode(s: String, flags: Int) = java.util.Base64.getMimeDecoder().decode(s)
}
object Log {
    const val DEBUG = 3
    fun isLoggable(tag: String, level: Int) = false
    fun d(tag: String, message: String) = 0
    fun i(tag: String, message: String) = 0
    fun w(tag: String, message: String, error: Throwable? = null) = 0
}
