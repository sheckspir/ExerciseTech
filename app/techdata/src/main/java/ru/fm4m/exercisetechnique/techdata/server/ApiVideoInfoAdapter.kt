package ru.fm4m.exercisetechnique.techdata.server

import com.google.gson.*
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo
import ru.fm4m.exercisetechnique.techdomain.data.YouTubeVideoInfo
import ru.fm4m.coredomain.system.Logger
import java.lang.reflect.Type

class ApiVideoInfoAdapter(private val logger: Logger) : JsonDeserializer<VideoInfo> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ): VideoInfo {
        if (json?.isJsonObject == true) {
            val type = (json as JsonObject).get("type")
            logger.d("TAG", "type = $type")
            if (type?.asString?.uppercase() == "YT") {
                return YouTubeVideoInfo(
                    json.get("id").asString,
                    json.get("name").asString,
                    json.get("hints").asString
                )
            }
        }
        return Gson().fromJson(json, VideoInfo::class.java)
    }
}