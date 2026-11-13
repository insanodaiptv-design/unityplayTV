package tv.own.owntv.core.update

import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class UpdateAssetSelectionTest {
    private fun assets(vararg names: String): JSONArray = JSONArray(names.mapIndexed { index, name ->
        JSONObject().put("name", name).put("browser_download_url", "https://example.test/$index")
    })

    @Test fun televisionSkipsPhoneAndOriginalApks() {
        val release = assets("unityplayTV-Mobile-5.1.2.apk", "OwnTV-v5.1.2.apk",
            "unityplayTV-AndroidTV-5.1.2.apk")
        assertEquals("https://example.test/2", findUpdateApk(release, wantX86 = false, isUnityplayTv = true))
    }

    @Test fun televisionSkipsIncompatibleArchitecture() {
        val release = assets("unityplayTV-AndroidTV-5.1.2.apk",
            "unityplayTV-AndroidTV-5.1.2-x86_64.apk")
        assertEquals("https://example.test/1", findUpdateApk(release, wantX86 = true, isUnityplayTv = true))
        assertEquals("https://example.test/0", findUpdateApk(release, wantX86 = false, isUnityplayTv = true))
    }

    @Test fun phoneOnlyReleaseHasNoTelevisionAsset() {
        val release = assets("unityplayTV-Mobile-5.1.2.apk", "unityplayTV-AndroidTV-5.1.2.zip")
        assertNull(findUpdateApk(release, wantX86 = false, isUnityplayTv = true))
    }

    @Test fun existingMobileSelectionStillWorks() {
        val release = assets("OwnTV-Mobile-x86_64-v5.1.2.apk", "OwnTV-Mobile-v5.1.2.apk")
        assertEquals("https://example.test/1", findUpdateApk(release, wantX86 = false, isUnityplayTv = false))
    }
}
