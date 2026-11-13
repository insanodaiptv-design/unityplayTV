package tv.own.owntv.core.update

import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MobileUpdateSelectionTest {
    private fun release(tag: String, vararg names: String): JSONObject =
        JSONObject().put("tag_name", tag).put("assets", JSONArray(names.mapIndexed { index, name ->
            JSONObject().put("name", name).put("browser_download_url", "https://example.test/$index")
        }))

    @Test fun emptyOrAlreadyInstalledReturnsNoUpdate() {
        assertNull(selectMobileUpdate(JSONArray(), "1.0.4", wantX86 = false))
        val installed = release("mobile-v1.0.4", "unityplayTV-Mobile-1.0.4.apk")
        assertNull(selectMobileUpdate(JSONArray(listOf(installed)), "1.0.4", wantX86 = false))
    }

    @Test fun findsHighestMobileVersionRegardlessOfReleaseOrder() {
        val releases = JSONArray(listOf(
            release("v5.1.2", "unityplayTV-AndroidTV-5.1.2.apk"),
            release("mobile-v1.0.10", "unityplayTV-Mobile-1.0.10.apk"),
            release("mobile-v1.0.9", "unityplayTV-Mobile-1.0.9.apk"),
        ))
        assertEquals("1.0.10", selectMobileUpdate(releases, "1.0.4", wantX86 = false)?.version)
    }

    @Test fun skipsDraftsPreviewsAndMismatchedApkVersions() {
        val releases = JSONArray(listOf(
            release("mobile-v1.0.8", "unityplayTV-Mobile-1.0.8.apk").put("draft", true),
            release("mobile-v1.0.7", "unityplayTV-Mobile-1.0.7.apk").put("prerelease", true),
            release("mobile-v1.0.6", "unityplayTV-Mobile-1.0.5.apk"),
            release("mobile-v1.0.5", "unityplayTV-AndroidTV-1.0.5.apk"),
        ))
        assertNull(selectMobileUpdate(releases, "1.0.4", wantX86 = false))
    }

    @Test fun choosesTheApkForThePhoneArchitecture() {
        val releases = JSONArray(listOf(release("mobile-v1.0.5",
            "unityplayTV-Mobile-1.0.5.apk", "unityplayTV-Mobile-1.0.5-x86_64.apk")))
        assertEquals("https://example.test/0", selectMobileUpdate(releases, "1.0.4", false)?.apkUrl)
        assertEquals("https://example.test/1", selectMobileUpdate(releases, "1.0.4", true)?.apkUrl)
    }
}
