package tv.own.owntv.features.shell.components

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.junit.Assert.*
import org.junit.Test

class PosterFallbackLoaderTest {
    @Test fun limitsConcurrentCatalogLookupsToTwo() = runBlocking {
        val started = Channel<Unit>(Channel.UNLIMITED)
        val release = CompletableDeferred<Unit>()
        val jobs = List(3) { async { PosterFallbackLoader.load { started.send(Unit); release.await(); "poster" } } }
        withTimeout(2000) { repeat(2) { started.receive() } }
        assertNull(withTimeoutOrNull(40) { started.receive() })
        release.complete(Unit)
        jobs.awaitAll().forEach { assertEquals("poster", it) }
    }

    @Test fun cancellationReleasesPermitForNextVisibleTile() = runBlocking {
        val started = CompletableDeferred<Unit>()
        val job = launch { PosterFallbackLoader.load { started.complete(Unit); awaitCancellation() } }
        started.await(); job.cancelAndJoin()
        assertEquals("next", withTimeout(2000) { PosterFallbackLoader.load { "next" } })
    }

    @Test fun lookupFailureDoesNotExhaustPermits() = runBlocking {
        repeat(3) {
            try { PosterFallbackLoader.load { throw IllegalStateException() }; fail() }
            catch (_: IllegalStateException) { }
        }
        assertEquals("recovered", withTimeout(2000) { PosterFallbackLoader.load { "recovered" } })
    }
}
