package ru.fm4m.testmodule

class LambdaManager(val name: String) {

    fun getSomeFunstion(doing : (String) -> Unit) {
        doing(name)
    }

    inline fun getSomeFunstion1(doing : (String) -> Unit) {
        doing(name)
    }

    inline fun getSomeFunstion2(noinline doing : (String) -> Unit) {
        doing(name)
    }

    inline fun getSomeFunstion3(crossinline doing: (String) -> Unit) {
        getSomeFunstion1(doing)
    }
}