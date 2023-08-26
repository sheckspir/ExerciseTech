package ru.fm4m.coredomain.system


class TestLogger : Logger {
    override fun d(tag: String, message: String) {
        //do nothing
    }

    override fun e(tag: String, message: String, e: Throwable?) {
        //do nothing
    }
}