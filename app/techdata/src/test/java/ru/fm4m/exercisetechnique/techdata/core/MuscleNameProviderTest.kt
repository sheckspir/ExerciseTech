package ru.fm4m.exercisetechnique.techdata.core

import android.content.res.Resources
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkClass
import org.junit.Test
import ru.fm4m.exercisetechnique.techdomain.data.Muscle
import ru.fm4m.exercisetechnique.techdomain.data.Sex

class MuscleNameProviderTest {

    @MockK
    private lateinit var resources: Resources

    private val muscleNameProvider: MuscleNameProvider

    init {
        MockKAnnotations.init(this)
        every {resources.getString(0)} returns "ERROR"
        every {resources.getString(0, any())} returns "ERROR"
        every { resources.getString(any()) } returns "OK"
        every { resources.getString(any(), any()) } returns "OK"

        muscleNameProvider = MuscleNameProvider(resources)
    }

    @Test
    fun muscle_new_value() {
        val newMuscle = mockkClass(Muscle::class)
        val single = muscleNameProvider.getMuscleInfo(newMuscle)
        val muscleInfo = single.test().values()

        assert(muscleInfo.size == 1)
        assert(muscleInfo[0].muscle == newMuscle)
        assert(muscleInfo[0].name == "")

    }

    @Test
    fun all_muscles_have_name() {
        val muscles = Muscle.values()

        for (one in muscles) {
            val musclesInfo = muscleNameProvider.getMuscleInfo(one).test().values()

            assert(musclesInfo.size == 1)
            assert(musclesInfo[0].name != "")
        }
    }

    @Test
    fun muscle_new_value_with_new_data() {
        val allSex = Sex.values()
        for (one in allSex) {
            val musclesInfo = muscleNameProvider.getListMusclesInfo(one).test().values()

            assert(musclesInfo.size > 0)
        }

    }


}