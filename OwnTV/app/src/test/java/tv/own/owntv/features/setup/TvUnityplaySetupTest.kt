package tv.own.owntv.features.setup
import org.junit.Assert.*
import org.junit.Test
class TvUnityplaySetupTest {
    @Test fun emptyCredentialsCannotStartImport() {
        assertFalse(TvSetupPolicy.canImport("", "", ""))
        assertFalse(TvSetupPolicy.canImport("http://example.test", "user", ""))
        assertFalse(TvSetupPolicy.canImport("http://example.test", " ", "pass"))
    }
    @Test fun completeCredentialsAllowImport() {
        assertTrue(TvSetupPolicy.canImport("http://example.test:8080", "user", "pass"))
    }
}
