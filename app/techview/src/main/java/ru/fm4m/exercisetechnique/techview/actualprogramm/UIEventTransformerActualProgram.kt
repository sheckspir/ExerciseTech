package ru.fm4m.exercisetechnique.techview.actualprogramm

import android.util.Log
import ru.fm4m.exercisetechnique.techview.core.UIEvent

sealed class UIEventActualProgram : UIEvent() {
    object DownloadProgram : UIEventActualProgram()
    data class ShowDay(val dayId : Int) : UIEventActualProgram()
}

class UIEventTransformerActualProgram : (UIEventActualProgram) -> ActualProgramFeature.Wish? {

    override fun invoke(p1: UIEventActualProgram): ActualProgramFeature.Wish {
        Log.d("TAG", "UIEventTransformerActualProgram.invoke $p1")
        return when(p1) {
            is UIEventActualProgram.DownloadProgram -> ActualProgramFeature.Wish.DownloadProgram
            is UIEventActualProgram.ShowDay -> ActualProgramFeature.Wish.ShowDay(p1.dayId)
        }
    }
}