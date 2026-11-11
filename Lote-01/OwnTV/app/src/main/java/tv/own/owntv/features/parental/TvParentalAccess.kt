package tv.own.owntv.features.parental

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.core.context.GlobalContext
import tv.own.owntv.R
import tv.own.owntv.core.content.AdultCategoryClassifier
import tv.own.owntv.core.database.dao.CategoryDao
import tv.own.owntv.core.database.dao.ProfileDao
import tv.own.owntv.core.settings.SettingsRepository
import tv.own.owntv.core.util.Pin
import tv.own.owntv.features.profiles.PinDialog

internal class ContentGrant {
    private var value: Pair<Long, String>? = null
    @Synchronized fun allow(key: Pair<Long, String>) { value = key }
    @Synchronized fun matches(key: Pair<Long, String>): Boolean = value == key
    @Synchronized fun consume(key: Pair<Long, String>): Boolean {
        if (value != key) return false
        value = null
        return true
    }
    @Synchronized fun revoke() { value = null }
}

internal object ParentalPinPolicy {
    fun requiresPin(categoryName: String?): Boolean = categoryName.isNullOrBlank() || AdultCategoryClassifier.isAdult(categoryName)
    fun verify(value: String, stored: String?): Boolean =
        if (stored.isNullOrBlank()) value == "0000" else Pin.verify(value, stored)
}

private fun pinHash(context: Context, profileId: Long): String? =
    context.getSharedPreferences("unityplay_parental", Context.MODE_PRIVATE).getString("pin_$profileId", null)

internal object TvParentalAccess {
    class Request(val profileId: Long, val hash: String?, val answer: CompletableDeferred<Boolean> = CompletableDeferred())
    val pending = MutableStateFlow<Request?>(null)
    private val mutex = Mutex()
    private val grant = ContentGrant()
    fun revoke() { grant.revoke() }

    suspend fun authorize(categoryId: Long?, contentKey: String, force: Boolean = false, consume: Boolean = false): Boolean = mutex.withLock {
        val koin = GlobalContext.get()
        val settings = koin.get<SettingsRepository>()
        val profiles = koin.get<ProfileDao>()
        val id = settings.activeProfileId.first()
        val profile = profiles.getById(id) ?: return@withLock false
        val category = categoryId?.let { koin.get<CategoryDao>().getById(it) }
        if (!ParentalPinPolicy.requiresPin(category?.name)) return@withLock true
        if (profile.isKids) return@withLock false
        val key = id to contentKey
        if (!force && (if (consume) grant.consume(key) else grant.matches(key))) return@withLock true
        revoke()
        val context = koin.get<Context>()
        val request = Request(id, pinHash(context, id))
        pending.value = request
        try {
            val accepted = request.answer.await() && settings.activeProfileId.first() == id
            if (accepted && !consume) grant.allow(key)
            accepted
        } finally {
            if (pending.value === request) pending.value = null
        }
    }
}

@Composable
internal fun ParentalPinHost() {
    val request by TvParentalAccess.pending.collectAsState()
    request?.let { pending ->
        var wrong by remember(pending) { mutableStateOf(false) }
        key(pending) {
            PinDialog(
                title = stringResource(if (wrong) R.string.unityplay_parental_wrong else R.string.unityplay_parental_title),
                onSubmit = { value ->
                    if (ParentalPinPolicy.verify(value, pending.hash)) pending.answer.complete(true)
                    else wrong = true
                },
                onDismiss = { pending.answer.complete(false) }, compact = true,
            )
        }
    }
}

@Composable
internal fun ParentalPinSettingsRow(profileId: Long) {
    val context = LocalContext.current
    val profiles = remember { GlobalContext.get().get<ProfileDao>() }
    var ready by remember(profileId) { mutableStateOf(false) }
    LaunchedEffect(profileId) { ready = profiles.getById(profileId) != null }
    var stage by remember(profileId) { mutableIntStateOf(0) }
    var candidate by remember { mutableStateOf("") }
    var wrong by remember { mutableStateOf(false) }
    tv.own.owntv.features.settings.StageSettingRow(
        icon = tv.own.owntv.ui.components.OwnTVIcon.PERSON,
        title = stringResource(R.string.unityplay_parental_title),
        desc = stringResource(R.string.unityplay_parental_change),
        value = null, enabled = ready, onClick = { wrong = false; stage = 1 },
        help = tv.own.owntv.features.settings.SettingHelp(stringResource(R.string.unityplay_parental_title), stringResource(R.string.unityplay_parental_change)),
    )
    if (stage > 0) key(stage) {
        PinDialog(
            title = stringResource(if (wrong) R.string.unityplay_parental_wrong else when (stage) {
                1 -> R.string.unityplay_parental_current
                2 -> R.string.unityplay_parental_new
                else -> R.string.unityplay_parental_confirm
            }),
            onSubmit = { value ->
                when (stage) {
                    1 -> if (ParentalPinPolicy.verify(value, pinHash(context, profileId))) { stage = 2; wrong = false } else wrong = true
                    2 -> if (value.length == 4) { candidate = value; stage = 3; wrong = false } else wrong = true
                    3 -> if (value == candidate) {
                        context.getSharedPreferences("unityplay_parental", Context.MODE_PRIVATE).edit().putString("pin_$profileId", Pin.hash(value)).apply()
                        TvParentalAccess.revoke(); candidate = ""; stage = 0
                    } else wrong = true
                }
            }, onDismiss = { candidate = ""; stage = 0 }, compact = true,
        )
    }
}

/** A focus preview never requests a PIN and never mounts sensitive metadata while resolving. */
@Composable
internal fun adultPreviewHidden(categoryId: Long?, itemId: Long): Boolean {
    var hidden by remember(categoryId, itemId) { mutableStateOf(true) }
    LaunchedEffect(categoryId, itemId) {
        hidden = runCatching {
            val category = categoryId?.let { GlobalContext.get().get<CategoryDao>().getById(it) }
            category == null || AdultCategoryClassifier.isAdult(category.name)
        }.getOrDefault(true)
    }
    return hidden
}
