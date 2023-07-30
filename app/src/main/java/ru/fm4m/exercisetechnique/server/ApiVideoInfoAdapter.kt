package ru.fm4m.exercisetechnique.server

import android.util.Log
import com.google.gson.*
import ru.fm4m.exercisetechnique.model.VideoInfo
import ru.fm4m.exercisetechnique.model.YouTubeVideoInfo
import java.lang.reflect.Type

class ApiVideoInfoAdapter : JsonDeserializer<VideoInfo> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ): VideoInfo {
        if (json?.isJsonObject == true) {
            val type = (json as JsonObject).get("type")
            Log.d("TAG", "type = $type")
            if (type.asString == "YT") {
                return YouTubeVideoInfo(json.get("id").asString, json.get("name").asString,json.get("hints").asString)
            }
        }
        return Gson().fromJson(json, VideoInfo::class.java);
    }
}