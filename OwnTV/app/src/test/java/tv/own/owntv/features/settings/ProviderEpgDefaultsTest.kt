package tv.own.owntv.features.settings
import org.junit.Assert.*
import org.junit.Test
class ProviderEpgDefaultsTest {
    @Test fun freshInstallUsesProvider() { assertTrue(shouldInstallProviderGuide(false, emptySet(), listOf("provider"))) }
    @Test fun migratedProviderCanReceiveRefreshDefault() { assertTrue(shouldInstallProviderGuide(false, setOf("provider"), listOf("provider"))) }
    @Test fun existingCustomGuideIsRespected() { assertFalse(shouldInstallProviderGuide(false, setOf("custom"), listOf("provider"))) }
    @Test fun deletingOrEditingProviderIsNotUndone() { assertFalse(shouldInstallProviderGuide(true, emptySet(), listOf("provider"))) }
    @Test fun absentFeedWaitsForDiscovery() { assertFalse(shouldInstallProviderGuide(false, emptySet(), emptyList())) }
}
