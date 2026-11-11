package tv.own.owntv.features.shell.components
import org.junit.Assert.*
import org.junit.Test
class PosterUrlPolicyTest {
    @Test fun absentAndSentinelUrlsRequestFallback() {
        listOf(null, "", " ", "null", "N/A", "file:///etc/file", "javascript:alert(1)").forEach { assertNull(PosterUrlPolicy.usable(it)) }
    }
    @Test fun providerHttpIsPreserved() { assertEquals("http://provider.test/cover.jpg", PosterUrlPolicy.usable(" http://provider.test/cover.jpg ")) }
    @Test fun protocolRelativeImagesUseHttps() { assertEquals("https://cdn.test/p.jpg", PosterUrlPolicy.usable("//cdn.test/p.jpg")) }
    @Test fun signedQueryIsPreserved() { assertEquals("https://cdn.test/p?a=1&b=2", PosterUrlPolicy.usable("https://cdn.test/p?a=1&b=2")) }
}
