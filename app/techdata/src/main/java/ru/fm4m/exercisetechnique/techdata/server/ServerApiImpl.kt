package ru.fm4m.exercisetechnique.techdata.server

import ru.fm4m.exercisetechnique.techdomain.data.Muscle
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.fm4m.exercisetechnique.techdomain.data.ProgramInfo
import ru.fm4m.exercisetechnique.techdomain.data.ProgramPartShort
import ru.fm4m.exercisetechnique.techdomain.data.ProgramPartInfo
import ru.fm4m.exercisetechnique.techdomain.server.ServerApi
import java.util.concurrent.TimeUnit

interface ServerApiBackend : ServerApi {

    @GET("get_videos")
    override fun getVideoList(@Query("male") male: Boolean, @Query("muscle") muscle: Muscle): Single<List<VideoInfo>>

    @GET("find_videos")
    override fun getVideosForKey(@Query("text") key: String): Single<List<VideoInfo>>

    @GET("actual_program")
    override fun getNewProgram(): Single<List<VideoInfo>>

    @GET("actual_program")
    override suspend fun getActualProgram(): ProgramInfo

    @GET("day_info")
    override suspend fun getDayInfo(dayId: Int): ProgramPartInfo
}

class ServerApiImpl : ServerApi {

    companion object {

        private val instance : ServerApi = ServerApiImpl()
        fun getInstance() = instance
    }
    private val videos : Map<Muscle, List<VideoInfo>> = HashMap<Muscle, List<VideoInfo>>().apply {

       /* put(Muscle.NECK, ArrayList<VideoInfo>().apply{
            add(YouTubeVideoInfo("ZznYty_zPpk", "Сгибание шеи лёжа", "сгибание шеи лёжа,тренировка шеи,шея")))
            add(YouTubeVideoInfo("NBklK-jMuSQ", "Разгибание шеи лёжа", "Разгибание шеи лёжа", "шея")))
            add(YouTubeVideoInfo("F9rEgz4vVeM", "Сгибание шеи лёжа", "Сгибание шеи лёжа", "шея")))
        })

        put(Muscle.TRAPECIA, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("3GEir9Tiens", "Шраги с гантелями", "Шраги с гантелями", "Шраги")))
        })

        put(Muscle.DELTA, ArrayList<VideoInfo>().apply {

            add(YouTubeVideoInfo("1-ut6uXyZ6o", null, "Махи гантелями через стороны сидя", "Махи гантелями в стороны сидя","дельты", "плечи","средний пучок")))
            add(YouTubeVideoInfo("UUdKpGSQgmc", null, "Жим гантелей стоя", "армейский жим","дельты", "плечи","средний пучок")))
            add(YouTubeVideoInfo("Y9x7blBNwJo", null, "ТЯГА ШТАНГИ К ПОДБОРОДКУ", "армейский жим","плечи","дельты")))
            add(YouTubeVideoInfo("4pqiISq4w2M", null, "РАЗВЕДЕНИЕ ГАНТЕЛЕЙ В НАКЛОНЕ", "махи ГАНТЕЛЕЙ В НАКЛОНЕ", "махи гантелей", "разведение гантелей", "дельты", "плечи","задний пучок")))
            add(YouTubeVideoInfo("dYiwJdn7wxw", null, "МАХИ ГАНТЕЛЯМИ Перед Собой", "махи перед собой", "махи гирей перед собой", "дельты", "плечи", "передний пучок")))
            add(YouTubeVideoInfo("0oj5pY3RQrc", null, "Жим Штанги Стоя", "армейский жим", "плечи", "дельты", "передний пучок")))
            add(YouTubeVideoInfo("XTwzjgaruqE", null, "Жим штанги из-за головы", "плечи", "дельты", "передний пучок")))
            add(YouTubeVideoInfo("6iZk8no8D3A", null, "Жим Арнольда","Жим гантелей стоя", "армейский жим", "плечи", "дельты" )))
            add(YouTubeVideoInfo("WnI2hZJ0d9w", null, "Махи гантелями в стороны", "плечи", "дельты" )))
            add(YouTubeVideoInfo("Pw97SOiOGyQ", null, "Протяжка", "протяжка со штангой", "плечи", "дельты" )))
            add(YouTubeVideoInfo("We1g2CnoVOY", null, "Протяжка", "протяжка в тренажере", "протяжка блока", "плечи", "дельты", "задний пучок", "задняя дельта")))
            add(YouTubeVideoInfo("uFTH_rqLQNI", null, "Жим на смите перед собой", "Жим штанги сидя в тренажере Смита", "Жим штанги сидя в Смите", "Жим сидя в Смите до середины", "плечи", "дельты")))
            add(YouTubeVideoInfo("1pBefch_wcc", "Жим из-за головы в Смите сидя", "Жим из-за головы в Смите сидя", "Жим штанги сидя в тренажере Смита", "Жим из-за головы","плечи", "дельты")))
            add(YouTubeVideoInfo("GBGI44G2Txg", null, "Отведение на Блоке одной рукой на заднюю дельту","Отведение в сторону на нижнем блоке", "Махи руками назад в кроссовере", "плечи","дельты")))
            add(YouTubeVideoInfo("Hdez37pVs3U", null, "Махи в сторону одной рукой в кроссовере", "МАХИ у Блока По Переменно каждой рукой", "махи в блоке на дельты", "махи у блока на плечи", "плечи", "дельты")))
            add(YouTubeVideoInfo("HDBiiEONxhE", null, "плечи", "дельты", "полное видео")))

        })
        put(Muscle.CHEST, ArrayList<VideoInfo>().apply {

            add(YouTubeVideoInfo("XUeE5cTzsfo", null, "Жим штанги лёжа классический", "Жим штанги на горизонтальной скамье", "жим лёжа", "грудь", "грудные")))
            add(YouTubeVideoInfo("JsWe_LgS3OU", null, "Жим штанги лёжа узким хватом", "Жим узким хватом", "Жим штанги Узким Хватом", "грудь", "грудные", "трицепс")))
            add(YouTubeVideoInfo("HuivGGB9Wlg", null, "Жим штанги лёжа под углом", "Жим штанги под углом", "Жим штанги на наклонной", "грудь", "грудные")))
            add(YouTubeVideoInfo("lBJuRIoqrrI", null, "Жим штанги с упоров", "Жим с упоров","грудь", "грудные")))
            add(YouTubeVideoInfo("OlZwuV_0TDo", null, "Жим штанги средним хватом", "грудь", "грудные")))
            add(YouTubeVideoInfo("e-8rXstgMCA", null, "Жим штанги в Смите", "грудь", "грудные")))
            add(YouTubeVideoInfo("eOxRFx8LBng", null, "Сведение рук в тренажёре", "Бабочка", "грудь", "грудные")))
            add(YouTubeVideoInfo("xwtaHancCQc", null, "Жим Гантелей Лежа На Наклонной Скамье", "Жим Гантелей На Наклонной Скамье", "Жим Гантелей под 30 градусов",  "Жим Гантелей под углом в 30 градусов","грудь", "грудные")))
            add(YouTubeVideoInfo("WaDPNbP3xWk", null, "Разведение Гантелей Лежа На Скамье", "Разведение Гантелей На Скамье", "Разведение Гантелей Лежа", "грудь", "грудные")))
            add(YouTubeVideoInfo("oW9GfQuhSVE", null, "Отжимания от пола", "грудь", "грудные")))
            add(YouTubeVideoInfo("Zpen5apxTk4", null, "Пуловер", "Пуловер У Блока", "грудь", "грудные")))
            add(YouTubeVideoInfo("1cunOUoh0RQ", null, "сведение рук в кроссовере","кроссовер", "грудь", "грудные")))
            add(YouTubeVideoInfo("ECVnG2Ct8ac", null, "Отжимания на брусьях, широким хватом","Отжимания на брусьях широким хватом", "грудь", "грудные")))



            add(YouTubeVideoInfo("RTlMlKB9rn0","Как накачать грудные. Версия 1", "Как накачать грудные. Версия 1", "грудь", "грудные", "полное видео")))

        })
        put(Muscle.BICEPS, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("63n8aYEhTbk", null, "Подъем EZ-грифа на бицепс у стены", "строгий подъем на бицепс", "бицепс")))
            add(YouTubeVideoInfo("P957YHBOs6Q", null, "Подъем Гантелей на Бицепс Сидя", "гантели на бицепс сидя", "бицепс")))
            add(YouTubeVideoInfo("FqF11YdwxrQ", null, "Подъем Штанги на Бицепс", "бицепс")))
            add(YouTubeVideoInfo("WZsf_lQCHWg", null, "Подъем гантели с супинацией", "Подъем гантелей с супинацией", "супинация", "подьем гантелей на бицепс", "бицепс")))
            add(YouTubeVideoInfo("bF4T6-Mh0MU", null, "Подтягивание обратным хватом", "подтягивания", "бицепс")))
            add(YouTubeVideoInfo("ygj-wYBU8X0", null, "Подъем Штанги Обратным Хватом", "Подъём EZ штанги обратным хватом", "бицепс обратным хватом", "подъем обратным хватом", "бицепс")))
            add(YouTubeVideoInfo("Pd4WUV-boGA", null, "Молоток", "молот", "подъём гантелей молот","Сгибание рук с гантелями молот", "Попеременное сгибание рук с гантелями", "бицепс")))
            add(YouTubeVideoInfo("db5N9RypciI", null, "Мощные руки", "бицепс", "трицепс", "предплечья", "полное видео")))
            add(YouTubeVideoInfo("D6q5VT-fGcs", null, "Тренировка рук от Андрей Скоромный", "бицепс", "трицепс", "полное видео")))
            add(YouTubeVideoInfo("QDzbUatc4dg", null, "Сгибание рук на скамье скотта", "Подьем на Бицепс на Ск Скотта", "бицепс на скамье скотта", "бицепс")))

        })

        put(Muscle.PREDPLECHIA, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("v0Tc-thEsbk", null, "Сгибание кистей со штангой сидя", "предплечья")))
            add(YouTubeVideoInfo("fEktPfDu36U", null, "Разгибание кистей со штангой сидя", "предплечья")))
            add(YouTubeVideoInfo("GUc0OsEXUCY", null, "Сгибание рук со штангой хватом сверху", "предплечья")))
            add(YouTubeVideoInfo("uVO3IWanU_Y", null, "Сгибание кисти с гантелей", "Сгибание Кистей с Гантелью Попеременно", "предплечья")))
            add(YouTubeVideoInfo("Tyx1WBWFV6Q", null, "Разгибания кистей с гантелями","Разгибание Кистей с Гантелью Попеременно", "предплечья")))
            add(YouTubeVideoInfo("db5N9RypciI", null, "Мощные руки", "бицепс", "трицепс", "предплечья", "полное видео")))
        })

        put(Muscle.KOSS_PRESS, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("JOlhML9opNE", null, "Планка", "пресс")))
            add(YouTubeVideoInfo("tX2KvWLlxwQ", null, "Боковые наклоны через козла","Боковые наклоны через гиперэкстензию", "косые")))
            add(YouTubeVideoInfo("ILSvcyRn7us", null, "Наклоны в бок с гантелей стоя","Наклоны в стороны с гантелей стоя", "косые")))
            add(YouTubeVideoInfo("WiZGHz_4was","Книжка", "книжка", "пресс")))
            add(YouTubeVideoInfo("JWG4ZRvS8n8", null, "Как накачать пресс", "пресс", "косые", "полное видео")))
        })

        put(Muscle.PRESS, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("ez-PsVSkWk8", null, "ОБРАТНЫЕ СКРУЧИВАНИЯ", "ПОДЪЕМ НОГ", "пресс")))
            add(YouTubeVideoInfo("POS7Wnwplwo","Обратные скручивания", "Обратные скручивания", "пресс")))
            add(YouTubeVideoInfo("JOlhML9opNE", null, "Планка", "пресс")))
            add(YouTubeVideoInfo("kBv1yyQN51E", null, "Скручивания", "Скручивание на полу", "пресс")))
            add(YouTubeVideoInfo("zAY_dFM6OC4", null, "Подъём ног в висе", "Скручивания в висе", "Подъем прямых ног к перекладине в висе", "пресс")))
            add(YouTubeVideoInfo("uli2PqMd2BA", null, "Подъём ног в висе", "Скручивания в висе", "Подъем прямых ног к перекладине в висе","пресс")))
            add(YouTubeVideoInfo("C84WM9wSrac","Вакуум", "вакуум", "пресс")))
            add(YouTubeVideoInfo("4dnFqdd61JE",null,  "скручивания у вертикального блока","Скручивания с верхнего блока","молитва", "пресс")))
            add(YouTubeVideoInfo("WiZGHz_4was","Книжка", "книжка", "пресс")))
            add(YouTubeVideoInfo("JWG4ZRvS8n8", null, "Как накачать пресс", "пресс", "косые", "полное видео")))
        })

        put(Muscle.KVADR, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("mo3S1nDTsx4", null, "приседания с упоров до середины", "квадрицепс")))
            add(YouTubeVideoInfo("u7WBL40UC1c", null, "приседания с упоров до середины с удержанием", "Приседания с Упоров до Середины  + удержание 2 сек", "квадрицепс")))
            add(YouTubeVideoInfo("mo3S1nDTsx4", null, "приседания с упоров до середины", "квадрицепс")))
            add(YouTubeVideoInfo("ULD3MHqt2Mg", null, "приседания со штангой", "ПРИСЕДАНИЯ классические","квадрицепс")))
            add(YouTubeVideoInfo("I8B5cSjiTao", null, "приседания со штангой", "ПРИСЕДАНИЯ классические", "квадрицепс")))
            add(YouTubeVideoInfo("IXrl60rgm4U", null, "приседания с гантелью между ног", "квадрицепс")))
            add(YouTubeVideoInfo("jAX69X9Lr1c", null, "Выпады", "квадрицепс", "ягодицы", "ягодичные")))
            add(YouTubeVideoInfo("qz8QvMGHJu0", null, "разгибание ног в тренажёре сидя", "Разгибание в Тренажере", "квадрицепс")))
            add(YouTubeVideoInfo("xS1KbQzAj7g", null, "Разгибание голени в тренажёре одной ногой", "Разгибание бедра в тренажёре одной ногой", "Разгибание в Тренажере ОДНОЙ ногой", "квадрицепс")))
            add(YouTubeVideoInfo("VhZn1y0wKnk", null, "Приседания с гантелями", "квадрицепс")))
            add(YouTubeVideoInfo("8bw-KDUknOc", null, "Сведение ног в тренажере")))
            add(YouTubeVideoInfo("dDUz97h35_M", null, "Разведение ног на тренажере")))
            add(YouTubeVideoInfo("3nMof73yOQ0", null, "Становая тяга в стиле СУМО", "квадрицепс", "широчайшие", "спина")))
            add(YouTubeVideoInfo("duSnLRneftc", null, "Становая тяга", "квадрицепс", "широчайшие", "спина")))
            add(YouTubeVideoInfo("IzwyH77jBOU", null, "Жим Платформы Одной Ногой", "квадрицепс", "ягодицы", "ягодичные")))
            add(YouTubeVideoInfo("Yy5wcN_9aQ0", null, "Болгарские Выпады", "Болгарские приседания","квадрицепс", "ягодицы", "ягодичные", "жопа")))
        })
        
        put(Muscle.GOLEN, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("uKaahmlHbMY", null, "Подъём на носки стоя", "икры", "голень")))
            add(YouTubeVideoInfo("mVe69zfPkfs", null, "Подъём на носки сидя", "икры", "голень")))
        })

        put(Muscle.TRICEPS, ArrayList<VideoInfo>().apply{
            add(YouTubeVideoInfo("ixbV51Cd5es", null, "Жим Гантели на Трицепс из-за Головы", "трицепс")))
            add(YouTubeVideoInfo("JoTWyGhTlhQ", null, "Французский жим лёжа", "Французский жим лежа", "трицепс")))
            add(YouTubeVideoInfo("oz_ecw_Pasc", null, "Французский жим с EZ-грифом ", "Французский жим лёжа", "Французский жим лежа", "трицепс")))
            add(YouTubeVideoInfo("WOOcjC2y-e4", null, "Французский жим стоя", "Французский жим сидя", "трицепс")))
            add(YouTubeVideoInfo("gt0Sz26hOCM", "Разгибание руки в наклоне", "Разгибание руки в наклоне", "Разгибание руки в упоре", "трицепс")))
            add(YouTubeVideoInfo("T8JTg6nrfxU", null, "Разгибание рук из-за головы в блоке", "Разгибание из-за головы стоя на блоке", "трицепс")))
            add(YouTubeVideoInfo("MdCT_6DN3hk", "Разгибание рук у вертикального блока", "Разгибание рук у вертикального блока", "Разгибание рук в вертикальном блоке", "трицепс")))
            add(YouTubeVideoInfo("JsWe_LgS3OU", null, "Жим штанги лёжа узким хватом", "Жим узким хватом", "грудь", "грудные", "трицепс")))
            add(YouTubeVideoInfo("OSWanefT124", null, "Отжимания от скамьи с упором сзади", "трицепс")))
            add(YouTubeVideoInfo("db5N9RypciI", null, "Мощные руки", "бицепс", "трицепс", "предплечья", "полное видео")))
            add(YouTubeVideoInfo("D6q5VT-fGcs", null, "Тренировка рук от Андрей Скоромный", "бицепс", "трицепс", "полное видео")))
        })

        put(Muscle.SHIROCH, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("cB3ztFmAXnU", null, "Тяга штанги в наклоне","Тяга Штанги в Наклоне Обратным Хватом", "широчайшие", "спина")))
            add(YouTubeVideoInfo("OALB21WhghY", "Тяга гантели к поясу", "Тяга гантели к поясу", "тяга гантели в наклоне", "широчайшие", "спина")))
            add(YouTubeVideoInfo("MCi1n7Y0_3Y", "Т-тяга", "Т-тяга", "Тяга Т-грифа", "т тяга", "тяга Т грифа", "широчайшие", "спина")))
            add(YouTubeVideoInfo("GjVvFGSc-JM", null, "Подтягивания широким хватом", "широчайшие", "спина")))
            add(YouTubeVideoInfo("uJGJ1Rdys3I", null, "Подтягивания узким хватом", "Подтягивания параллельным хватом", "широчайшие", "спина")))
            add(YouTubeVideoInfo("bF4T6-Mh0MU", null, "Подтягивания обратным хватом", "Подтягивания средним хватом", "широчайшие", "спина")))
            add(YouTubeVideoInfo("9e77z6YzBx0", null, "тяга вертикального блока", "Тяга к груди с верхнего блока широким хватом", "Тяга с верхнего блока широким хватом", "широчайшие", "спина")))
            add(YouTubeVideoInfo("h0MqHmsYavg", null, "Тяга с верхнего блока узким параллельным хватом", "вертикальная тяга параллельным хватом", "широчайшие", "спина")))
            add(YouTubeVideoInfo("CtP0tKV6DdM", null, "тяга горизонтального блока", "тяга горизонтального блока узким хватом", "широчайшие", "спина")))
            add(YouTubeVideoInfo("duSnLRneftc", null, "Становая тяга", "широчайшие", "спина")))
            add(YouTubeVideoInfo("pV501RQm864", null, "Становая тяга", "Мертвая тяга", "широчайшие", "спина", "бицепс бедра", "ягодицы", "ягодичные", "жопа" )))
            add(YouTubeVideoInfo("IsWtXXv1E8o", null, "Становая тяга", "Мертвая тяга", "широчайшие", "спина", "ягодицы", "ягодичные", "жопа")))
            add(YouTubeVideoInfo("ws8kM6EaLaA", null, "Гиперэкстензия", "экстензия", "широчайшие", "спина")))
            add(YouTubeVideoInfo("kBdGViTsJ_Q", null, "Тренировка спины Яковина", "полное видео", "широчайшие", "спина")))
        })

        put(Muscle.BUTT, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("jAX69X9Lr1c", null, "Выпады", "квадрицепс", "ягодицы", "ягодичные", "жопа")))
            add(YouTubeVideoInfo("jFC-wIcUgok", null, "Мертвая тяга", "ягодицы", "ягодичные", "жопа", "бицепс бедра")))
            add(YouTubeVideoInfo("IsWtXXv1E8o", null, "Становая тяга", "Мертвая тяга", "широчайшие", "спина", "ягодицы", "ягодичные", "жопа")))
            add(YouTubeVideoInfo("pV501RQm864", null, "Становая тяга", "Мертвая тяга", "широчайшие", "спина", "бицепс бедра", "ягодицы", "ягодичные", "жопа" )))
            add(YouTubeVideoInfo("dDUz97h35_M", null, "Разведение ног на тренажере")))
            add(YouTubeVideoInfo("miHtt5dN9Oo", null, "Гиперэкстензия", "экстензия", "ягодицы", "ягодичные", "жопа")))
            add(YouTubeVideoInfo("IzwyH77jBOU", null, "Жим Платформы Одной Ногой", "квадрицепс", "ягодицы", "ягодичные", "жопа")))
            add(YouTubeVideoInfo("Yy5wcN_9aQ0", null, "Болгарские Выпады", "Болгарские приседания","квадрицепс", "ягодицы", "ягодичные", "жопа")))


        })

        put(Muscle.BICEPS_BEDR, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("wXLwjcYAWR8", null, "сгибание ног с гантелей", "сгибание ног лёжа", "бицепс бедра")))
            add(YouTubeVideoInfo("jFC-wIcUgok", null, "Мертвая тяга", "ягодицы", "ягодичные", "жопа", "бицепс бедра")))
            add(YouTubeVideoInfo("pV501RQm864", null, "Становая тяга", "Мертвая тяга", "широчайшие", "спина", "бицепс бедра", "ягодицы", "ягодичные", "жопа" )))
            add(YouTubeVideoInfo("hSFM7VAZELM", null, "сгибание ног лёжа", "бицепс бедра" )))
            add(YouTubeVideoInfo("S1vGMpdqdzM", null, "Сгибание Голени Одной Ногой", "Сгибание ноги в тренажёре стоя", "бицепс бедра" )))
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


        */
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
                                if (!videosForKeys.containsKey(oneVideo.hints)) {
                                    videosForKeys[oneVideo.hints] = ArrayList<VideoInfo>().apply {
                                        add(oneVideo)
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

    override fun getNewProgram(): Single<List<VideoInfo>> {
        return Completable.timer(100, TimeUnit.MILLISECONDS)
            .andThen(Single.fromCallable {
                return@fromCallable ArrayList<VideoInfo>().apply {
                    //1
                    /*
                    add(YouTubeVideoInfo("xwtaHancCQc", null, "Жим Гантелей Лежа На Наклонной Скамье", "Жим Гантелей На Наклонной Скамье", "Жим Гантелей под 30 градусов",  "Жим Гантелей под углом в 30 градусов","грудь", "грудные")))
                    add(YouTubeVideoInfo("GjVvFGSc-JM", null, "Подтягивания широким хватом", "широчайшие", "спина")))
                    add(YouTubeVideoInfo("uKaahmlHbMY", null, "Подъём на носки стоя", "икры", "голень")))
                    add(YouTubeVideoInfo("JsWe_LgS3OU", null, "Жим штанги лёжа узким хватом", "Жим узким хватом", "грудь", "грудные", "трицепс")))
                    add(YouTubeVideoInfo("T8JTg6nrfxU", null, "Разгибание рук из-за головы в блоке", "Разгибание из-за головы стоя на блоке", "трицепс")))
                    add(YouTubeVideoInfo("MCi1n7Y0_3Y", "Т-тяга", "Т-тяга", "Тяга Т-грифа", "т тяга", "тяга Т грифа", "широчайшие", "спина")))
                    add(YouTubeVideoInfo("Pd4WUV-boGA", null, "Молоток", "молот", "подъём гантелей молот","Сгибание рук с гантелями молот", "Попеременное сгибание рук с гантелями", "бицепс")))

                    //2
                    add(YouTubeVideoInfo("ULD3MHqt2Mg", null, "приседания со штангой", "ПРИСЕДАНИЯ классические","квадрицепс")))
                    add(YouTubeVideoInfo("1pBefch_wcc", "Жим из-за головы в Смите сидя", "Жим из-за головы в Смите сидя", "Жим штанги сидя в тренажере Смита", "Жим из-за головы","плечи", "дельты")))
                    add(YouTubeVideoInfo("zAY_dFM6OC4", null, "Подъём ног в висе", "Скручивания в висе", "Подъем прямых ног к перекладине в висе", "пресс")))
                    add(YouTubeVideoInfo("qz8QvMGHJu0", null, "разгибание ног в тренажёре сидя", "Разгибание в Тренажере", "квадрицепс")))
                    add(YouTubeVideoInfo("hSFM7VAZELM", null, "сгибание ног лёжа", "бицепс бедра" )))
                    add(YouTubeVideoInfo("4pqiISq4w2M", null, "РАЗВЕДЕНИЕ ГАНТЕЛЕЙ В НАКЛОНЕ", "махи ГАНТЕЛЕЙ В НАКЛОНЕ", "махи гантелей", "разведение гантелей", "дельты", "плечи","задний пучок")))
                    add(YouTubeVideoInfo("kBv1yyQN51E", null, "Скручивания", "Скручивание на полу", "пресс")))

                    //3
                    add(YouTubeVideoInfo("XUeE5cTzsfo", null, "Жим штанги лёжа классический", "Жим штанги на горизонтальной скамье", "жим лёжа", "грудь", "грудные")))
                    add(YouTubeVideoInfo("h0MqHmsYavg", null, "Тяга с верхнего блока узким параллельным хватом", "вертикальная тяга параллельным хватом", "широчайшие", "спина")))
                    add(YouTubeVideoInfo("mVe69zfPkfs", null, "Подъём на носки сидя", "икры", "голень")))
                    add(YouTubeVideoInfo("ECVnG2Ct8ac", null, "Отжимания на брусьях, широким хватом","Отжимания на брусьях широким хватом", "грудь", "грудные")))
                    add(YouTubeVideoInfo("OALB21WhghY", "Тяга гантели к поясу", "Тяга гантели к поясу", "тяга гантели в наклоне", "широчайшие", "спина")))
                    add(YouTubeVideoInfo("JoTWyGhTlhQ", null, "Французский жим лёжа", "Французский жим лежа", "трицепс")))

                    //4
                    add(YouTubeVideoInfo("LL0uQJthemo", null, "Жим одной ногой в тренажёре", "квадрицепсы", "ягодицы", "ягодичные", "жопа")))
                    add(YouTubeVideoInfo("Y9x7blBNwJo", null, "ТЯГА ШТАНГИ К ПОДБОРОДКУ", "армейский жим","плечи","дельты")))
                    add(YouTubeVideoInfo("kBv1yyQN51E", null, "Скручивания", "Скручивание на полу", "пресс")))
                    add(YouTubeVideoInfo("Yy5wcN_9aQ0", null, "Болгарские Выпады", "Болгарские приседания","квадрицепс", "ягодицы", "ягодичные", "жопа")))
                    add(YouTubeVideoInfo("GBGI44G2Txg", null, "Отведение на Блоке одной рукой на заднюю дельту","Отведение в сторону на нижнем блоке", "Махи руками назад в кроссовере", "плечи","дельты")))
                    add(YouTubeVideoInfo("ez-PsVSkWk8", null, "ОБРАТНЫЕ СКРУЧИВАНИЯ", "ПОДЪЕМ НОГ", "пресс")))
                    add(YouTubeVideoInfo("POS7Wnwplwo","Обратные скручивания", "Обратные скручивания", "пресс")))







                    add(YouTubeVideoInfo("Tyx1WBWFV6Q", null, "Разгибания кистей с гантелями","Разгибание Кистей с Гантелью Попеременно", "предплечья")))


//                    add(YouTubeVideoInfo("u7WBL40UC1c", null, "приседания с упоров до середины с удержанием", "Приседания с Упоров до Середины  + удержание 2 сек", "квадрицепс")))
                    add(YouTubeVideoInfo("sEzewPzO3Yo", null, "Наклоны со штангой на плечах","Наклоны со штангой", "упражнение доброе утро", "ягодичные", "жопа", "бицепс бедра")))

//
//                    add(YouTubeVideoInfo("xS1KbQzAj7g", null, "Разгибание голени в тренажёре одной ногой", "Разгибание бедра в тренажёре одной ногой", "Разгибание в Тренажере ОДНОЙ ногой", "квадрицепс")))
//                    add(YouTubeVideoInfo("zAY_dFM6OC4", null, "Подъём ног в висе", "Скручивания в висе", "Подъем прямых ног к перекладине в висе", "пресс")))
//                    add(YouTubeVideoInfo("uli2PqMd2BA", null, "Подъём ног в висе", "Скручивания в висе", "Подъем прямых ног к перекладине в висе","пресс")))


                    add(YouTubeVideoInfo("OlZwuV_0TDo", null, "Жим штанги средним хватом", "грудь", "грудные")))
                    add(YouTubeVideoInfo("uFTH_rqLQNI", null, "Жим на смите перед собой", "Жим штанги сидя в тренажере Смита", "Жим штанги сидя в Смите", "Жим сидя в Смите до середины", "плечи", "дельты")))
                    add(YouTubeVideoInfo("uJGJ1Rdys3I", null, "Подтягивания узким хватом", "Подтягивания параллельным хватом", "широчайшие", "спина")))

                    add(YouTubeVideoInfo("oz_ecw_Pasc", null, "Французский жим с EZ-грифом ", "Французский жим лёжа", "Французский жим лежа", "трицепс")))
                    add(YouTubeVideoInfo("uVO3IWanU_Y", null, "Сгибание кисти с гантелей", "Сгибание Кистей с Гантелью Попеременно", "предплечья")))

                    add(YouTubeVideoInfo("duSnLRneftc", null, "Становая тяга", "квадрицепс", "широчайшие", "спина")))

                    add(YouTubeVideoInfo("CtP0tKV6DdM", null, "тяга горизонтального блока", "тяга горизонтального блока узким хватом", "широчайшие", "спина")))

*/
                }
            })
    }

    override suspend fun getActualProgram(): ProgramInfo {
        val programDays = ArrayList<ProgramPartShort>().apply {
            add(ProgramPartShort(0, "1", "", 5))
            add(ProgramPartShort(0, "2", "", 6))
            add(ProgramPartShort(0, "3", "", 5))
            add(ProgramPartShort(0, "4", "", 6))
        }
        return ProgramInfo(
            0,
            "Очень крутая программа",
            "Программа рассчитана на 4 недели, каждый день выпоняется особенным образом.\n" +
                    "День 1: Икры всегда 5*25\n" +
                    "Неделя 1: Все упражнения 5*8\n" +
                    "Неделя 2: Все упражнения 5*15\n" +
                    "Неделя 3: Дропсеты\n" +
                    "Неделя 4: Все 6*12\n" +
                    "Дропсеты: 2 разогревочных + 1 подводящий + 1 рабочий на максимум 8 повторений, сразу же скидываем 30% и выполняем максимум, скидываем 30% и на максимум\n" +
                    "\n" +
                    "День2: Подъём ног на висе всегда 3*MAX\n" +
                    "Велосипед 20 минут после каждой тренировки\n" +
                    "Неделя 1: Обратная пирамида\n" +
                    "Неделя 2: 5*15\n" +
                    "Неделя 3: 5*8\n" +
                    "Неделя 4: 6*12\n" +
                    "Обратная пирамида: 2 разогрвочных, 1 подводящий\n" +
                    "1 Рабочий вес х 10 повторений\n" +
                    "Отдых 2 минуты\n" +
                    "2 -20% х 15 повторений\n" +
                    "Отдых 2 минуты\n" +
                    "3 -20% х 20 повторений\n" +
                    "\n" +
                    "День 3: Подъём на носки сидя 5х15\n" +
                    "Неделя 1: 5*8\n" +
                    "Неделя 2: 5*15\n" +
                    "Неделя 3: 6*12\n" +
                    "Неделя 4: Прямая пирамида\n" +
                    "Прямая пирамида: 2 разогревочных, 1 подводящий\n" +
                    "1 Рабочий х 20 повторений\n" +
                    "Отдых 2 минуты\n" +
                    "2 +20% х 15 повторений\n" +
                    "Отдых 2 минуты\n" +
                    "3 +20% 10 повторений\n" +
                    "\n" +
                    "День 4: Обратные скручивания 3хМАКС, Велосипед в конце тренировки 20 минут\n" +
                    "Неделя 1: 5х8\n" +
                    "Неделя 2: Отдых-пауза\n" +
                    "Неделя 3: 5х15\n" +
                    "Неделя 4: 6х12\n" +
                    "Отдых-пауза: 2 Разогревочных + 1 подводящий\n" +
                    "{\n" +
                    "1 Рабочий х 8 повторений\n" +
                    "Отдых 15 секунд\n" +
                    "2 Рабочий на 4 повторения\n" +
                    "Отдых 15 секунд\n" +
                    "3 Рабочий на 2-3 повторений\n" +
                    "} (повторить 2 раза)",
            programDays
        )
    }

    override suspend fun getDayInfo(dayId: Int): ProgramPartInfo {
        TODO("Not yet implemented")
    }
}