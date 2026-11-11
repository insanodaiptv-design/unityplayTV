import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.CancellationException
import tv.own.owntv.core.database.entity.SourceEntity
import tv.own.owntv.core.network.HttpClient
import tv.own.owntv.core.parser.XtreamClient

// Runs the complete production XtreamClient.kt. Android JSON APIs are supplied by Gson;
// only network and Android logging/clock are fixture adapters. No real credentials are used.
fun main() = runBlocking {
    val source = SourceEntity()
    var cases = 0
    for (section in listOf("movies", "series")) {
        val idField = if (section == "movies") "stream_id" else "series_id"
        val fixture = "[{\"$idField\":101,\"name\":\"Fixture title\",\"category_id\":\"7\"}]"
        for (bulk in listOf("{}", "{\"error\":\"bulk unavailable\"}", "null", "\"unavailable\"")) {
            val requests = mutableListOf<String>()
            val http = HttpClient { url -> requests += url; if (url.contains("category_id=")) fixture else bulk }
            val client = XtreamClient(http)
            val titles = mutableListOf<String>()
            val complete = if (section == "movies") client.streamVod(source, onItem = { titles += it.name })
                else client.streamSeries(source, onItem = { titles += it.name })
            check(!complete) { "$section: non-array response must request fallback, got success for $bulk" }
            val recovered = if (section == "movies") client.streamVod(source, "7", onItem = { titles += it.name })
                else client.streamSeries(source, "7", onItem = { titles += it.name })
            check(recovered && titles == listOf("Fixture title"))
            check(requests.size == 2 && requests.last().contains("&category_id=7"))
            cases++
        }
        for (body in listOf("[]", fixture)) {
            val client = XtreamClient(HttpClient { body })
            val titles = mutableListOf<String>()
            val complete = if (section == "movies") client.streamVod(source, onItem = { titles += it.name })
                else client.streamSeries(source, onItem = { titles += it.name })
            check(complete); check(titles.size == if (body == "[]") 0 else 1); cases++
        }
        val truncated = fixture.dropLast(1) + ", {\"name\":\"cut"
        val client = XtreamClient(HttpClient { truncated })
        val titles = mutableListOf<String>()
        val complete = if (section == "movies") client.streamVod(source, onItem = { titles += it.name })
            else client.streamSeries(source, onItem = { titles += it.name })
        check(!complete && titles == listOf("Fixture title")); cases++
    }
    val liveTitles = mutableListOf<String>()
    check(XtreamClient(HttpClient { "[{\"stream_id\":9,\"name\":\"Live fixture\"}]" })
        .streamLive(source, onItem = { liveTitles += it.name }))
    check(liveTitles == listOf("Live fixture")); cases++
    try {
        XtreamClient(HttpClient { throw CancellationException("cancel fixture") }).streamVod(source, onItem = {})
        error("Cancellation must propagate")
    } catch (expected: CancellationException) { cases++ }
    println("PASS: $cases Xtream parser and fallback scenarios")
}
