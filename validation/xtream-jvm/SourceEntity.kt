package tv.own.owntv.core.database.entity
data class SourceEntity(
    val id: Long = 1, val url: String = "http://fixture.invalid",
    val username: String? = "test", val password: String? = "test", val userAgent: String? = null,
)
