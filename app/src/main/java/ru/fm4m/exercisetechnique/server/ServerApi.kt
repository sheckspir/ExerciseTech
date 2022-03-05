package ru.fm4m.exercisetechnique.server

import ru.fm4m.exercisetechnique.model.Muscle
import ru.fm4m.exercisetechnique.model.VideoInfo
import ru.fm4m.exercisetechnique.model.YouTubeVideoInfo
import io.reactivex.Completable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

interface ServerApi {

    fun getVideoList(male: Boolean, muscle: Muscle): Single<List<VideoInfo>>

    fun getVideosForKey(key : String): Single<List<VideoInfo>>

    fun getNewProgramm() : Single<List<VideoInfo>>
}

class ServerApiImpl : ServerApi {

    companion object {

        private val instance : ServerApi = ServerApiImpl()
        fun getInstance() = instance
    }
    private val videos : Map<Muscle, List<VideoInfo>> = HashMap<Muscle, List<VideoInfo>>().apply {

        put(Muscle.NECK, ArrayList<VideoInfo>().apply{
            add(YouTubeVideoInfo("ZznYty_zPpk", "Сгибание шеи лёжа", arrayListOf("сгибание шеи лёжа", "тренировка шеи", "шея")))
            add(YouTubeVideoInfo("NBklK-jMuSQ", "Разгибание шеи лёжа", arrayListOf("Разгибание шеи лёжа", "шея")))
            add(YouTubeVideoInfo("F9rEgz4vVeM", "Сгибание шеи лёжа", arrayListOf("Сгибание шеи лёжа", "шея")))
        })

        put(Muscle.TRAPECIA, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("3GEir9Tiens", "Шраги с гантелями", arrayListOf("Шраги с гантелями", "Шраги")))
        })

        put(Muscle.DELTA, ArrayList<VideoInfo>().apply {

            add(YouTubeVideoInfo("1-ut6uXyZ6o", null, arrayListOf("Махи гантелями через стороны сидя", "Махи гантелями в стороны сидя","дельты", "плечи","средний пучок")))
            add(YouTubeVideoInfo("UUdKpGSQgmc", null, arrayListOf("Жим гантелей стоя", "армейский жим","дельты", "плечи","средний пучок")))
            add(YouTubeVideoInfo("Y9x7blBNwJo", null, arrayListOf("ТЯГА ШТАНГИ К ПОДБОРОДКУ", "армейский жим","плечи","дельты")))
            add(YouTubeVideoInfo("4pqiISq4w2M", null, arrayListOf("РАЗВЕДЕНИЕ ГАНТЕЛЕЙ В НАКЛОНЕ", "махи ГАНТЕЛЕЙ В НАКЛОНЕ", "махи гантелей", "разведение гантелей", "дельты", "плечи","задний пучок")))
            add(YouTubeVideoInfo("dYiwJdn7wxw", null, arrayListOf("МАХИ ГАНТЕЛЯМИ Перед Собой", "махи перед собой", "махи гирей перед собой", "дельты", "плечи", "передний пучок")))
            add(YouTubeVideoInfo("0oj5pY3RQrc", null, arrayListOf("Жим Штанги Стоя", "армейский жим", "плечи", "дельты", "передний пучок")))
            add(YouTubeVideoInfo("XTwzjgaruqE", null, arrayListOf("Жим штанги из-за головы", "плечи", "дельты", "передний пучок")))
            add(YouTubeVideoInfo("6iZk8no8D3A", null, arrayListOf("Жим Арнольда","Жим гантелей стоя", "армейский жим", "плечи", "дельты" )))
            add(YouTubeVideoInfo("WnI2hZJ0d9w", null, arrayListOf("Махи гантелями в стороны", "плечи", "дельты" )))
            add(YouTubeVideoInfo("Pw97SOiOGyQ", null, arrayListOf("Протяжка", "протяжка со штангой", "плечи", "дельты" )))
            add(YouTubeVideoInfo("We1g2CnoVOY", null, arrayListOf("Протяжка", "протяжка в тренажере", "протяжка блока", "плечи", "дельты", "задний пучок", "задняя дельта")))
            add(YouTubeVideoInfo("uFTH_rqLQNI", null, arrayListOf("Жим на смите перед собой", "Жим штанги сидя в тренажере Смита", "Жим штанги сидя в Смите", "Жим сидя в Смите до середины", "плечи", "дельты")))

            add(YouTubeVideoInfo("HDBiiEONxhE", null, arrayListOf("плечи", "дельты", "полное видео")))
        })
        put(Muscle.CHEST, ArrayList<VideoInfo>().apply {

            add(YouTubeVideoInfo("XUeE5cTzsfo", null, arrayListOf("Жим штанги лёжа классический", "жим лёжа", "грудь", "грудные")))
            add(YouTubeVideoInfo("JsWe_LgS3OU", null, arrayListOf("Жим штанги лёжа узким хватом", "Жим узким хватом", "грудь", "грудные", "трицепс")))
            add(YouTubeVideoInfo("HuivGGB9Wlg", null, arrayListOf("Жим штанги лёжа под углом", "Жим штанги под углом", "Жим штанги на наклонной", "грудь", "грудные")))
            add(YouTubeVideoInfo("lBJuRIoqrrI", null, arrayListOf("Жим штанги с упоров", "Жим с упоров","грудь", "грудные")))
            add(YouTubeVideoInfo("OlZwuV_0TDo", null, arrayListOf("Жим штанги средним хватом", "грудь", "грудные")))
            add(YouTubeVideoInfo("e-8rXstgMCA", null, arrayListOf("Жим штанги в Смите", "грудь", "грудные")))

            add(YouTubeVideoInfo("eOxRFx8LBng", null, arrayListOf("Сведение рук в тренажёре", "Бабочка", "грудь", "грудные")))

            add(YouTubeVideoInfo("xwtaHancCQc", null, arrayListOf("Жим Гантелей Лежа На Наклонной Скамье", "Жим Гантелей На Наклонной Скамье", "Жим Гантелей под 30 градусов", "грудь", "грудные")))
            add(YouTubeVideoInfo("WaDPNbP3xWk", null, arrayListOf("Разведение Гантелей Лежа На Скамье", "Разведение Гантелей На Скамье", "Разведение Гантелей Лежа", "грудь", "грудные")))
            add(YouTubeVideoInfo("oW9GfQuhSVE", null, arrayListOf("Отжимания от пола", "грудь", "грудные")))
            add(YouTubeVideoInfo("Zpen5apxTk4", null, arrayListOf("Пуловер", "Пуловер У Блока", "грудь", "грудные")))
            add(YouTubeVideoInfo("1cunOUoh0RQ", null, arrayListOf("сведение рук в кроссовере","кроссовер", "грудь", "грудные")))

            add(YouTubeVideoInfo("RTlMlKB9rn0","Как накачать грудные. Версия 1", arrayListOf("Как накачать грудные. Версия 1", "грудь", "грудные", "полное видео")))

        })
        put(Muscle.BICEPS, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("63n8aYEhTbk", null, arrayListOf("Подъем EZ-грифа на бицепс у стены", "строгий подъем на бицепс", "бицепс")))
            add(YouTubeVideoInfo("P957YHBOs6Q", null, arrayListOf("Подъем Гантелей на Бицепс Сидя", "гантели на бицепс сидя", "бицепс")))
            add(YouTubeVideoInfo("FqF11YdwxrQ", null, arrayListOf("Подъем Штанги на Бицепс", "бицепс")))
            add(YouTubeVideoInfo("WZsf_lQCHWg", null, arrayListOf("Подъем гантели с супинацией", "Подъем гантелей с супинацией", "супинация", "подьем гантелей на бицепс", "бицепс")))
            add(YouTubeVideoInfo("bF4T6-Mh0MU", null, arrayListOf("Подтягивание обратным хватом", "подтягивания", "бицепс")))
            add(YouTubeVideoInfo("ygj-wYBU8X0", null, arrayListOf("Подъем Штанги Обратным Хватом", "Подъём EZ штанги обратным хватом", "бицепс обратным хватом", "подъем обратным хватом", "бицепс")))
            add(YouTubeVideoInfo("Pd4WUV-boGA", null, arrayListOf("Молоток", "молот", "подъём гантелей молот","Сгибание рук с гантелями молот", "бицепс")))

            add(YouTubeVideoInfo("db5N9RypciI", null, arrayListOf("Мощные руки", "бицепс", "трицепс", "предплечья", "полное видео")))
            add(YouTubeVideoInfo("D6q5VT-fGcs", null, arrayListOf("Тренировка рук от Андрей Скоромный", "бицепс", "трицепс", "полное видео")))

        })

        put(Muscle.PREDPLECHIA, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("v0Tc-thEsbk", null, arrayListOf("Сгибание кистей со штангой сидя", "предплечья")))
            add(YouTubeVideoInfo("fEktPfDu36U", null, arrayListOf("Разгибание кистей со штангой сидя", "предплечья")))
            add(YouTubeVideoInfo("GUc0OsEXUCY", null, arrayListOf("Сгибание рук со штангой хватом сверху", "предплечья")))
            add(YouTubeVideoInfo("uVO3IWanU_Y", null, arrayListOf("Сгибание кисти с гантелей", "Сгибание Кистей с Гантелью Попеременно", "предплечья")))
            add(YouTubeVideoInfo("Tyx1WBWFV6Q", null, arrayListOf("Разгибания кистей с гантелями","Разгибание Кистей с Гантелью Попеременно", "предплечья")))

            add(YouTubeVideoInfo("db5N9RypciI", null, arrayListOf("Мощные руки", "бицепс", "трицепс", "предплечья", "полное видео")))
        })

        put(Muscle.KOSS_PRESS, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("JOlhML9opNE", null, arrayListOf("Планка", "пресс")))
            add(YouTubeVideoInfo("tX2KvWLlxwQ", null, arrayListOf("Боковые наклоны через козла","Боковые наклоны через гиперэкстензию", "косые")))
            add(YouTubeVideoInfo("ILSvcyRn7us", null, arrayListOf("Наклоны в бок с гантелей стоя","Наклоны в стороны с гантелей стоя", "косые")))
            add(YouTubeVideoInfo("WiZGHz_4was","Книжка", arrayListOf("книжка", "пресс")))

            add(YouTubeVideoInfo("JWG4ZRvS8n8", null, arrayListOf("Как накачать пресс", "пресс", "косые", "полное видео")))
        })

        put(Muscle.PRESS, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("ez-PsVSkWk8", null, arrayListOf("ОБРАТНЫЕ СКРУЧИВАНИЯ", "ПОДЪЕМ НОГ", "пресс")))
            add(YouTubeVideoInfo("JOlhML9opNE", null, arrayListOf("Планка", "пресс")))
            add(YouTubeVideoInfo("kBv1yyQN51E", null, arrayListOf("Скручивания", "пресс")))
            add(YouTubeVideoInfo("zAY_dFM6OC4", null, arrayListOf("Подъём ног в висе", "Скручивания в висе", "Подъем прямых ног к перекладине в висе", "пресс")))
            add(YouTubeVideoInfo("uli2PqMd2BA", null, arrayListOf("Подъём ног в висе", "Скручивания в висе", "Подъем прямых ног к перекладине в висе","пресс")))
            add(YouTubeVideoInfo("POS7Wnwplwo","Обратные скручивания", arrayListOf("Обратные скручивания", "пресс")))
            add(YouTubeVideoInfo("C84WM9wSrac","Вакуум", arrayListOf("вакуум", "пресс")))
            add(YouTubeVideoInfo("4dnFqdd61JE",null,  arrayListOf("скручивания у вертикального блока","Скручивания с верхнего блока","молитва", "пресс")))
            add(YouTubeVideoInfo("WiZGHz_4was","Книжка", arrayListOf("книжка", "пресс")))

            add(YouTubeVideoInfo("JWG4ZRvS8n8", null, arrayListOf("Как накачать пресс", "пресс", "косые", "полное видео")))
        })

        put(Muscle.KVADR, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("mo3S1nDTsx4", null, arrayListOf("приседания с упоров до середины", "квадрицепс")))
            add(YouTubeVideoInfo("u7WBL40UC1c", null, arrayListOf("приседания с упоров до середины с удержанием", "Приседания с Упоров до Середины  + удержание 2 сек", "квадрицепс")))
            add(YouTubeVideoInfo("mo3S1nDTsx4", null, arrayListOf("приседания с упоров до середины", "квадрицепс")))
            add(YouTubeVideoInfo("ULD3MHqt2Mg", null, arrayListOf("приседания со штангой", "ПРИСЕДАНИЯ классические","квадрицепс")))
            add(YouTubeVideoInfo("I8B5cSjiTao", null, arrayListOf("приседания со штангой", "ПРИСЕДАНИЯ классические", "квадрицепс")))
            add(YouTubeVideoInfo("IXrl60rgm4U", null, arrayListOf("приседания с гантелью между ног", "квадрицепс")))
            add(YouTubeVideoInfo("jAX69X9Lr1c", null, arrayListOf("Выпады", "квадрицепс", "ягодицы", "ягодичные")))
            add(YouTubeVideoInfo("qz8QvMGHJu0", null, arrayListOf("разгибание ног в тренажёре сидя", "Разгибание в Тренажере", "квадрицепс")))
            add(YouTubeVideoInfo("xS1KbQzAj7g", null, arrayListOf("Разгибание голени в тренажёре одной ногой", "Разгибание бедра в тренажёре одной ногой", "Разгибание в Тренажере ОДНОЙ ногой", "квадрицепс")))
            add(YouTubeVideoInfo("VhZn1y0wKnk", null, arrayListOf("Приседания с гантелями", "квадрицепс")))
            add(YouTubeVideoInfo("8bw-KDUknOc", null, arrayListOf("Сведение ног в тренажере")))
            add(YouTubeVideoInfo("dDUz97h35_M", null, arrayListOf("Разведение ног на тренажере")))
            add(YouTubeVideoInfo("3nMof73yOQ0", null, arrayListOf("Становая тяга в стиле СУМО", "квадрицепс", "широчайшие", "спина")))
            add(YouTubeVideoInfo("duSnLRneftc", null, arrayListOf("Становая тяга", "квадрицепс", "широчайшие", "спина")))
            add(YouTubeVideoInfo("IzwyH77jBOU", null, arrayListOf("Жим Платформы Одной Ногой", "квадрицепс", "ягодицы", "ягодичные")))
        })
        
        put(Muscle.GOLEN, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("uKaahmlHbMY", null, arrayListOf("Подъём на носки стоя", "икры", "голень")))
            add(YouTubeVideoInfo("mVe69zfPkfs", null, arrayListOf("Подъём на носки сидя", "икры", "голень")))
        })

        put(Muscle.TRICEPS, ArrayList<VideoInfo>().apply{
            add(YouTubeVideoInfo("ixbV51Cd5es", null, arrayListOf("Жим Гантели на Трицепс из-за Головы", "трицепс")))
            add(YouTubeVideoInfo("JoTWyGhTlhQ", null, arrayListOf("Французский жим лёжа", "Французский жим лежа", "трицепс")))
            add(YouTubeVideoInfo("oz_ecw_Pasc", null, arrayListOf("Французский жим с EZ-грифом ", "Французский жим лёжа", "Французский жим лежа", "трицепс")))
            add(YouTubeVideoInfo("WOOcjC2y-e4", null, arrayListOf("Французский жим стоя", "Французский жим сидя", "трицепс")))
            add(YouTubeVideoInfo("gt0Sz26hOCM", "Разгибание руки в наклоне", arrayListOf("Разгибание руки в наклоне", "Разгибание руки в упоре", "трицепс")))
            add(YouTubeVideoInfo("MdCT_6DN3hk", "Разгибание рук у вертикального блока", arrayListOf("Разгибание рук у вертикального блока", "Разгибание рук в вертикальном блоке", "трицепс")))
            add(YouTubeVideoInfo("JsWe_LgS3OU", null, arrayListOf("Жим штанги лёжа узким хватом", "Жим узким хватом", "грудь", "грудные", "трицепс")))
            add(YouTubeVideoInfo("OSWanefT124", null, arrayListOf("Отжимания от скамьи с упором сзади", "трицепс")))

            add(YouTubeVideoInfo("db5N9RypciI", null, arrayListOf("Мощные руки", "бицепс", "трицепс", "предплечья", "полное видео")))
            add(YouTubeVideoInfo("D6q5VT-fGcs", null, arrayListOf("Тренировка рук от Андрей Скоромный", "бицепс", "трицепс", "полное видео")))
        })

        put(Muscle.SHIROCH, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("cB3ztFmAXnU", null, arrayListOf("Тяга штанги в наклоне","Тяга Штанги в Наклоне Обратным Хватом", "широчайшие", "спина")))
            add(YouTubeVideoInfo("OALB21WhghY", "Тяга гантели к поясу", arrayListOf("Тяга гантели к поясу", "тяга гантели в наклоне", "широчайшие", "спина")))
            add(YouTubeVideoInfo("MCi1n7Y0_3Y", "Т-тяга", arrayListOf("Т-тяга", "т тяга", "тяга Т грифа", "широчайшие", "спина")))
            add(YouTubeVideoInfo("GjVvFGSc-JM", null, arrayListOf("Подтягивания широким хватом", "широчайшие", "спина")))
            add(YouTubeVideoInfo("uJGJ1Rdys3I", null, arrayListOf("Подтягивания узким хватом", "Подтягивания параллельным хватом", "широчайшие", "спина")))
            add(YouTubeVideoInfo("bF4T6-Mh0MU", null, arrayListOf("Подтягивания обратным хватом", "Подтягивания средним хватом", "широчайшие", "спина")))
            add(YouTubeVideoInfo("9e77z6YzBx0", null, arrayListOf("тяга вертикального блока", "Тяга к груди с верхнего блока широким хватом", "Тяга с верхнего блока широким хватом", "широчайшие", "спина")))
            add(YouTubeVideoInfo("CtP0tKV6DdM", null, arrayListOf("тяга горизонтального блока", "тяга горизонтального блока узким хватом", "широчайшие", "спина")))
            add(YouTubeVideoInfo("duSnLRneftc", null, arrayListOf("Становая тяга", "широчайшие", "спина")))
            add(YouTubeVideoInfo("pV501RQm864", null, arrayListOf("Становая тяга", "Мертвая тяга", "широчайшие", "спина", "бицепс бедра", "ягодицы", "ягодичные", "жопа" )))
            add(YouTubeVideoInfo("IsWtXXv1E8o", null, arrayListOf("Становая тяга", "Мертвая тяга", "широчайшие", "спина", "ягодицы", "ягодичные", "жопа")))
            add(YouTubeVideoInfo("ws8kM6EaLaA", null, arrayListOf("Гиперэкстензия", "экстензия", "широчайшие", "спина")))

            add(YouTubeVideoInfo("kBdGViTsJ_Q", null, arrayListOf("Тренировка спины Яковина", "полное видео", "широчайшие", "спина")))
        })

        put(Muscle.BUTT, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("jAX69X9Lr1c", null, arrayListOf("Выпады", "квадрицепс", "ягодицы", "ягодичные", "жопа")))
            add(YouTubeVideoInfo("jFC-wIcUgok", null, arrayListOf("Мертвая тяга", "ягодицы", "ягодичные", "жопа", "бицепс бедра")))
            add(YouTubeVideoInfo("IsWtXXv1E8o", null, arrayListOf("Становая тяга", "Мертвая тяга", "широчайшие", "спина", "ягодицы", "ягодичные", "жопа")))
            add(YouTubeVideoInfo("pV501RQm864", null, arrayListOf("Становая тяга", "Мертвая тяга", "широчайшие", "спина", "бицепс бедра", "ягодицы", "ягодичные", "жопа" )))
            add(YouTubeVideoInfo("dDUz97h35_M", null, arrayListOf("Разведение ног на тренажере")))
            add(YouTubeVideoInfo("miHtt5dN9Oo", null, arrayListOf("Гиперэкстензия", "экстензия", "ягодицы", "ягодичные", "жопа")))
            add(YouTubeVideoInfo("IzwyH77jBOU", null, arrayListOf("Жим Платформы Одной Ногой", "квадрицепс", "ягодицы", "ягодичные", "жопа")))
        })

        put(Muscle.BICEPS_BEDR, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("wXLwjcYAWR8", null, arrayListOf("сгибание ног с гантелей", "сгибание ног лёжа", "бицепс бедра")))
            add(YouTubeVideoInfo("jFC-wIcUgok", null, arrayListOf("Мертвая тяга", "ягодицы", "ягодичные", "жопа", "бицепс бедра")))
            add(YouTubeVideoInfo("pV501RQm864", null, arrayListOf("Становая тяга", "Мертвая тяга", "широчайшие", "спина", "бицепс бедра", "ягодицы", "ягодичные", "жопа" )))
            add(YouTubeVideoInfo("hSFM7VAZELM", null, arrayListOf("сгибание ног лёжа", "бицепс бедра" )))
            add(YouTubeVideoInfo("S1vGMpdqdzM", null, arrayListOf("Сгибание Голени Одной Ногой", "Сгибание ноги в тренажёре стоя", "бицепс бедра" )))
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

    private val videosForKeys : HashMap<String, MutableList<VideoInfo>> = HashMap()

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

    override fun getVideosForKey(key: String): Single<List<VideoInfo>> {
        return Completable.timer(10, TimeUnit.MILLISECONDS)
            .andThen(
                Single.fromCallable {
                    if (videosForKeys.isEmpty()) {
                        for (videosList in videos.values) {
                            for (oneVideo in videosList) {
                                for(oneKey in oneVideo.keys) {
                                    if (!videosForKeys.containsKey(oneKey)) {
                                        videosForKeys[oneKey] = ArrayList<VideoInfo>().apply {
                                            add(oneVideo)
                                        }
                                    } else {
                                        videosForKeys[oneKey]?.add(oneVideo)
                                    }
                                }

                            }
                        }
                    }
                    val result = ArrayList<VideoInfo>()
                    for ((keys, value) in videosForKeys) {
                        if (keys.contains(key, true)) {
                            result.addAll(value)
                        }
                    }
                    return@fromCallable result
                }
            )
    }

    override fun getNewProgramm(): Single<List<VideoInfo>> {
        return Completable.timer(100, TimeUnit.MILLISECONDS)
            .andThen(Single.fromCallable {
                return@fromCallable ArrayList<VideoInfo>().apply {
                    add(YouTubeVideoInfo("XUeE5cTzsfo", null, arrayListOf("Жим штанги лёжа классический", "жим лёжа", "грудь", "грудные")))
                    add(YouTubeVideoInfo("lBJuRIoqrrI", null, arrayListOf("Жим штанги с упоров", "Жим с упоров","грудь", "грудные")))
                    add(YouTubeVideoInfo("GjVvFGSc-JM", null, arrayListOf("Подтягивания широким хватом", "широчайшие", "спина")))
                    add(YouTubeVideoInfo("1-ut6uXyZ6o", null, arrayListOf("Махи гантелями через стороны сидя", "Махи гантелями в стороны сидя","дельты", "плечи","средний пучок")))
                    add(YouTubeVideoInfo("63n8aYEhTbk", null, arrayListOf("Подъем EZ-грифа на бицепс у стены", "строгий подъем на бицепс", "бицепс")))
                    add(YouTubeVideoInfo("Tyx1WBWFV6Q", null, arrayListOf("Разгибания кистей с гантелями","Разгибание Кистей с Гантелью Попеременно", "предплечья")))

                    add(YouTubeVideoInfo("ULD3MHqt2Mg", null, arrayListOf("приседания со штангой", "ПРИСЕДАНИЯ классические","квадрицепс")))
                    add(YouTubeVideoInfo("u7WBL40UC1c", null, arrayListOf("приседания с упоров до середины с удержанием", "Приседания с Упоров до Середины  + удержание 2 сек", "квадрицепс")))
                    add(YouTubeVideoInfo("jFC-wIcUgok", null, arrayListOf("Мертвая тяга", "ягодицы", "ягодичные", "жопа", "бицепс бедра")))
                    add(YouTubeVideoInfo("IsWtXXv1E8o", null, arrayListOf("Становая тяга", "Мертвая тяга", "широчайшие", "спина", "ягодицы", "ягодичные", "жопа")))
                    add(YouTubeVideoInfo("qz8QvMGHJu0", null, arrayListOf("разгибание ног в тренажёре сидя", "Разгибание в Тренажере", "квадрицепс")))
                    add(YouTubeVideoInfo("xS1KbQzAj7g", null, arrayListOf("Разгибание голени в тренажёре одной ногой", "Разгибание бедра в тренажёре одной ногой", "Разгибание в Тренажере ОДНОЙ ногой", "квадрицепс")))
                    add(YouTubeVideoInfo("zAY_dFM6OC4", null, arrayListOf("Подъём ног в висе", "Скручивания в висе", "Подъем прямых ног к перекладине в висе", "пресс")))
                    add(YouTubeVideoInfo("uli2PqMd2BA", null, arrayListOf("Подъём ног в висе", "Скручивания в висе", "Подъем прямых ног к перекладине в висе","пресс")))

                    add(YouTubeVideoInfo("OlZwuV_0TDo", null, arrayListOf("Жим штанги средним хватом", "грудь", "грудные")))
                    add(YouTubeVideoInfo("uFTH_rqLQNI", null, arrayListOf("Жим на смите перед собой", "Жим штанги сидя в тренажере Смита", "Жим штанги сидя в Смите", "Жим сидя в Смите до середины", "плечи", "дельты")))
                    add(YouTubeVideoInfo("bF4T6-Mh0MU", null, arrayListOf("Подтягивания обратным хватом", "Подтягивания средним хватом", "широчайшие", "спина")))
                    add(YouTubeVideoInfo("Y9x7blBNwJo", null, arrayListOf("ТЯГА ШТАНГИ К ПОДБОРОДКУ", "армейский жим","плечи","дельты")))
                    add(YouTubeVideoInfo("oz_ecw_Pasc", null, arrayListOf("Французский жим с EZ-грифом ", "Французский жим лёжа", "Французский жим лежа", "трицепс")))
                    add(YouTubeVideoInfo("uVO3IWanU_Y", null, arrayListOf("Сгибание кисти с гантелей", "Сгибание Кистей с Гантелью Попеременно", "предплечья")))

                    add(YouTubeVideoInfo("duSnLRneftc", null, arrayListOf("Становая тяга", "квадрицепс", "широчайшие", "спина")))
                    add(YouTubeVideoInfo("cB3ztFmAXnU", null, arrayListOf("Тяга штанги в наклоне","Тяга Штанги в Наклоне Обратным Хватом", "широчайшие", "спина")))
                    add(YouTubeVideoInfo("Zpen5apxTk4", null, arrayListOf("Пуловер", "Пуловер У Блока", "грудь", "грудные")))
                    add(YouTubeVideoInfo("S1vGMpdqdzM", null, arrayListOf("Сгибание Голени Одной Ногой", "Сгибание ноги в тренажёре стоя", "бицепс бедра" )))
                    add(YouTubeVideoInfo("kBv1yyQN51E", null, arrayListOf("Скручивания", "пресс")))
                }
            })
    }
}