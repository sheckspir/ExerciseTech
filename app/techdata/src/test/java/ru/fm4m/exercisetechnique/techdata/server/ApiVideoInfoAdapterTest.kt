package ru.fm4m.exercisetechnique.techdata.server

import com.google.gson.JsonParser
import io.mockk.InternalPlatformDsl.toStr
import org.junit.Test
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo
import ru.fm4m.exercisetechnique.techdomain.data.YouTubeVideoInfo
import ru.fm4m.coredomain.system.TestLogger

class ApiVideoInfoAdapterTest {

    private val apiAdapter = ApiVideoInfoAdapter(TestLogger())


    @Test
    fun test_incorrect_json() {

    }

    @Test
    fun name_changed_on_file_videoinfo_without_expecting() {
        val defaultId = "1"
        val defaultUrl = "url1"
        val defaultTitle = "title1"
        val defaultHints = "hints1"
        val json = JsonParser().parse("{id : \"$defaultId\", url : \"$defaultUrl\", name: \"$defaultTitle\", hints : \"$defaultHints\"}")

        try {
            val videoInfo = apiAdapter.deserialize(json, null, null)

            assert(videoInfo.id.toString() == defaultId)
            assert(videoInfo.url == defaultUrl)
            assert(videoInfo.name == defaultTitle)
            assert(videoInfo.hints == defaultHints)
        } catch (e: Throwable) {

            assert(false)
        }


    }

    @Test
    fun name_changed_on_file_ytinfo_without_expecting() {
        val defaultType = "yt"
        val defaultId = "lalala"
        val defaultUrl = "url1"
        val defaultTitle = "title1"
        val defaultHints = "hints1"
        val json = JsonParser().parse("{id : \"$defaultId\", url : \"$defaultUrl\", name: \"$defaultTitle\", hints : \"$defaultHints\", type : \"$defaultType\"}")

        try {
            val videoInfo = apiAdapter.deserialize(json, null, null)

            assert(videoInfo is YouTubeVideoInfo)
            if (videoInfo is YouTubeVideoInfo) {
                assert(videoInfo.id == defaultId.hashCode())
                assert(videoInfo.youTubeId == defaultId)
                assert(videoInfo.url.contains(defaultId))
                assert(videoInfo.name == defaultTitle)
                assert(videoInfo.hints == defaultHints)
            }
        } catch (e: Throwable) {

            assert(false) { e.stackTraceToString() }
        }
    }

    @Test
    fun new_type_from_server() {
        val defaultType = "hyzqweasd"
        val defaultId = "112312"
        val defaultUrl = "url1"
        val defaultTitle = "title1"
        val defaultHints = "hints1"
        val json = JsonParser().parse("{id : \"$defaultId\", url : \"$defaultUrl\", name: \"$defaultTitle\", hints : \"$defaultHints\", type : \"$defaultType\"}")

        try {
            val videoInfo = apiAdapter.deserialize(json, null, null)

            assert(videoInfo is VideoInfo)
            assert(videoInfo.id.toStr() == defaultId)
            assert(videoInfo.url == defaultUrl)
            assert(videoInfo.name == defaultTitle)
            assert(videoInfo.hints == defaultHints)
        } catch (e: Throwable) {
            assert(false) { e.stackTraceToString() }
        }
    }

    @Test
    fun no_url_unexpected_from_server() {
        val defaultType = "hyzqweasd"
        val defaultId = "112312"
        val defaultTitle = "title1"
        val defaultHints = "hints1"
        val json = JsonParser().parse("{id : \"$defaultId\", name: \"$defaultTitle\", hints : \"$defaultHints\", type : \"$defaultType\"}")

        try {
            apiAdapter.deserialize(json, null, null)
            assert(false) {"We can't be here, because no URL getted from server, it should be Exception"}
        } catch (_: Throwable) {
        }
    }

    @Test
    fun something_changed_on_server() {
        val json = JsonParser().parse("{something : \"UNEXPECTED \"}")

        try {
            apiAdapter.deserialize(json, null, null)
            assert(false) {"We can't be here, because no URL getted from server, it should be Exception"}
        } catch (_: Throwable) {
        }
    }

}