package tv.own.owntv.features.settings

import android.content.Context
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.core.context.GlobalContext
import tv.own.owntv.core.epg.EpgSourceStore
import tv.own.owntv.core.repository.EpgRepository
import tv.own.owntv.core.repository.SourceRepository
import tv.own.owntv.core.settings.SettingsRepository
import tv.own.owntv.core.settings.EpgRefresh
import tv.own.owntv.core.settings.EpgAutoRefresh
import tv.own.owntv.core.sync.work.EpgSyncScheduler

/** One-time default; editing/deleting the feed afterwards is an explicit user choice. */
internal fun shouldInstallProviderGuide(checked: Boolean, existing: Set<String>, provider: List<String>): Boolean =
    !checked && provider.isNotEmpty() && (existing.isEmpty() || provider.any { it in existing })

internal object ProviderEpgDefaults {
    private val mutex = Mutex()
    suspend fun observe(profileId: Long) {
        val koin = GlobalContext.get()
        val repository = koin.get<SourceRepository>()
        val epg = koin.get<EpgRepository>()
        val store = koin.get<EpgSourceStore>()
        val settings = koin.get<SettingsRepository>()
        val scheduler = koin.get<EpgSyncScheduler>()
        val prefs = koin.get<Context>().getSharedPreferences("unityplay_provider_epg", Context.MODE_PRIVATE)
        repository.observeSources(profileId).collect { sources ->
            mutex.withLock {
                // Snapshot before the loop: all playlists in a fresh install get their own guide.
                val existing = store.getAll().map { it.url }.toSet()
                for (source in sources) {
                    val key = source.id.toString()
                    if (prefs.getBoolean(key, false)) continue
                    try {
                        val urls = epg.guideUrls(source)
                        if (urls.isEmpty()) continue // catalog import may still be discovering the feed
                        if (shouldInstallProviderGuide(false, existing, urls)) {
                            for (url in urls) {
                                val feed = store.ensure(source.name, url, source.userAgent)
                                if (feed.id !in settings.epgAutoRefresh.first()) {
                                    settings.setEpgAutoRefresh(feed.id, EpgRefresh(EpgAutoRefresh.HOURS_12))
                                }
                                if (feed.lastSyncAt == null) scheduler.enqueueSync(feed.id, "provider_default", policy = androidx.work.ExistingWorkPolicy.KEEP)
                            }
                        }
                        prefs.edit().putBoolean(key, true).apply()
                    } catch (cancelled: CancellationException) {
                        throw cancelled
                    } catch (_: Exception) {
                        // Leave unchecked so a transient local failure may retry on the next startup.
                    }
                }
            }
        }
    }
}
