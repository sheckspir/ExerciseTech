package ru.fm4m.testmodule

import android.util.Log

val staticClass = TestClass.InnerClass("staticClass")
val secondStaticClass = TestClass.InnerClass("secondStaticClass")

fun one() {
    Log.d("TAG","did function one")
    privateFileFun()
}

private fun privateFileFun() {

}

fun TestClass.InnerClass.someFun() : String {
    return this.name + 1

}

inline fun <reified T> List<T>.addInfo() : List<T> {

    return this
}

class TestClass  {

    private fun privateTestFun() {

    }


    val justObject = InnerClass("justObject")

    companion object {
        val coObject = InnerClass("coObject")
        val coObject2 = InnerClass("coObject2")

        fun second() {
            Log.d("TAG", "did fun second")
        }
    }



    class InnerClass(val name : String) {
        init {
            Log.d("TAG", "init innerClass $name")
        }
    }

}

