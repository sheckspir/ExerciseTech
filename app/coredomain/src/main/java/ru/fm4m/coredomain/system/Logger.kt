package ru.fm4m.coredomain.system

interface Logger {
    fun d(tag: String = "TAG", message: String)
    fun e(tag: String = "TAG", message: String, e: Throwable? = null)
}