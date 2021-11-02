package com.example.exercisetechnique.server

import com.example.exercisetechnique.model.Muscle
import com.example.exercisetechnique.model.VideoInfo
import com.example.exercisetechnique.model.YouTubeVideoInfo
import io.reactivex.Completable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

interface ServerApi {

    fun getVideoList(male: Boolean, muscle: Muscle): Single<List<VideoInfo>>
}

class ServerApiImpl : ServerApi {

    companion object {

        private val instance : ServerApi = ServerApiImpl()
        fun getInstance() = instance
    }
    private val videos : Map<Muscle, List<VideoInfo>> = HashMap<Muscle, List<VideoInfo>>().apply {

        put(Muscle.NECK, ArrayList<VideoInfo>().apply{
            add(YouTubeVideoInfo("NvL6jeV05Wk"))
        })

        put(Muscle.TRAPECIA, ArrayList<VideoInfo>().apply {
            add((YouTubeVideoInfo("8DoT2seNzCs")))
        }) // TODO: 10/30/21 add шраги

        put(Muscle.DELTA, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("HDBiiEONxhE"))
            add(YouTubeVideoInfo("UUdKpGSQgmc"))
            add(YouTubeVideoInfo("Y9x7blBNwJo"))
            //todo добавить махи разведение через стороны,
            add(YouTubeVideoInfo("4pqiISq4w2M"))
            add(YouTubeVideoInfo("dYiwJdn7wxw"))
            add(YouTubeVideoInfo("0oj5pY3RQrc"))
            //todo добавить жим штанги стоя вариант 2
            add(YouTubeVideoInfo("6iZk8no8D3A"))
            //todo добавить жим штанги из-за головы
        })
        put(Muscle.CHEST, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("RTlMlKB9rn0","Как накачать грудные. Версия 1"))
            //todo добавить жим штанги под углом
            add(YouTubeVideoInfo("xwtaHancCQc"))
            add(YouTubeVideoInfo("WaDPNbP3xWk"))
            add(YouTubeVideoInfo("oW9GfQuhSVE"))
            //todo добавить сведение рук в кроссовере
            //todo добавить бабочка
            //todo добавить полувер
            //todo добавить жим штанги лёжа

        })
        put(Muscle.BICEPS, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("D6q5VT-fGcs"))
            add(YouTubeVideoInfo("db5N9RypciI"))
            add(YouTubeVideoInfo("P957YHBOs6Q"))
            add(YouTubeVideoInfo("FqF11YdwxrQ"))
            //todo добавить подъём штанги с супинацией
            //todo добавить подтягивания обратным хватом
            add(YouTubeVideoInfo("ygj-wYBU8X0"))
            //todo добавить концентрированный подъём
            //todo добавить молоток

        })

        put(Muscle.PREDPLECHIA, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("db5N9RypciI"))
        })

        put(Muscle.KOSS_PRESS, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("JWG4ZRvS8n8"))
            add(YouTubeVideoInfo("ez-PsVSkWk8"))
            add(YouTubeVideoInfo("JOlhML9opNE"))
            add(YouTubeVideoInfo("kBv1yyQN51E"))
            //todo добавить скручивания в висе
            add(YouTubeVideoInfo("POS7Wnwplwo","Обратные скручивания"))
            //todo добавить вакуум
            //todo добавить скручивания у вертикального блока
            add(YouTubeVideoInfo("WiZGHz_4was","Книжка"))
        })

        put(Muscle.PRESS, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("JWG4ZRvS8n8"))
            add(YouTubeVideoInfo("ez-PsVSkWk8"))
            add(YouTubeVideoInfo("JOlhML9opNE"))
            add(YouTubeVideoInfo("kBv1yyQN51E"))
            //todo добавить скручивания в висе
            add(YouTubeVideoInfo("POS7Wnwplwo","Обратные скручивания"))
            //todo добавить вакуум
            //todo добавить скручивания у вертикального блока
            add(YouTubeVideoInfo("WiZGHz_4was","Книжка"))
        })

        put(Muscle.KVADR, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("ULD3MHqt2Mg"))
            add(YouTubeVideoInfo("I8B5cSjiTao"))
            add(YouTubeVideoInfo("IXrl60rgm4U"))
            add(YouTubeVideoInfo("jAX69X9Lr1c"))
            // TODO: 10/30/21 add разгибание ног в тренажёре 
            add(YouTubeVideoInfo("VhZn1y0wKnk"))
            add(YouTubeVideoInfo("8bw-KDUknOc"))
            add(YouTubeVideoInfo("dDUz97h35_M"))
            add(YouTubeVideoInfo("3nMof73yOQ0"))
            add(YouTubeVideoInfo("IzwyH77jBOU"))
        })
        
        put(Muscle.GOLEN, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("uKaahmlHbMY"))
            add(YouTubeVideoInfo("mVe69zfPkfs"))
        // TODO: 10/30/21 add подъём стоя на носки
        })

        put(Muscle.TRICEPS, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("D6q5VT-fGcs"))
            add(YouTubeVideoInfo("db5N9RypciI"))
            add(YouTubeVideoInfo("ixbV51Cd5es"))
            //отжимания от скамьи с упором сзади
            // жим штанги узким хватом
            // французский жим
            add(YouTubeVideoInfo("gt0Sz26hOCM", "Разгибание руки в наклоне"))
            add(YouTubeVideoInfo("MdCT_6DN3hk", "Разгибание рук у вертикального блока"))
        })

        put(Muscle.SHIROCH, ArrayList<VideoInfo>().apply {
            //как накачать спину
            add(YouTubeVideoInfo("kBdGViTsJ_Q"))
            add(YouTubeVideoInfo("cB3ztFmAXnU"))
            //подтягивания
            //тяга вертикального блока
            //тяга горизонтального блока
            //становая тяга
            //подтягивания обратным хватом
            add(YouTubeVideoInfo("OALB21WhghY", "Тяга гантели к поясу"))
            add(YouTubeVideoInfo("MCi1n7Y0_3Y", "Т-тяга"))
            add(YouTubeVideoInfo("ws8kM6EaLaA"))
        })

        put(Muscle.BUTT, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("jAX69X9Lr1c"))
            add(YouTubeVideoInfo("jFC-wIcUgok"))
            add(YouTubeVideoInfo("pV501RQm864"))
            add(YouTubeVideoInfo("dDUz97h35_M"))
            add(YouTubeVideoInfo("miHtt5dN9Oo"))
            add(YouTubeVideoInfo("IzwyH77jBOU"))
        })

        put(Muscle.BICEPS_BEDR, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("wXLwjcYAWR8"))
            add(YouTubeVideoInfo("jFC-wIcUgok"))
            add(YouTubeVideoInfo("pV501RQm864"))
            //сгибание ног лёжа
        })

        put(Muscle.BRAIN, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("8LkT5nbjqws"))
            add(YouTubeVideoInfo("Is_Z_0SL8aA"))
            add(YouTubeVideoInfo("KLXR0BhakwY"))
            add(YouTubeVideoInfo("4TQEUiQ3HlM"))
            add(YouTubeVideoInfo("5_Jl1OpDiGI"))
            add(YouTubeVideoInfo("bC8AYhWnrtc"))
            add(YouTubeVideoInfo("ndW2PQpUNKk"))
            add(YouTubeVideoInfo("J4QBZf8dMMw"))
            add(YouTubeVideoInfo("fH7KJliQJkI"))
        })

    }

    override fun getVideoList(male: Boolean, muscle: Muscle): Single<List<VideoInfo>> {
        return Completable.timer(10, TimeUnit.MILLISECONDS)
            .andThen(
                Single.fromCallable {
                    return@fromCallable if (videos.containsKey(muscle)) {
                        videos[muscle]?: emptyList()
                    } else {
                        emptyList()
                    }
                }
            )
    }
}