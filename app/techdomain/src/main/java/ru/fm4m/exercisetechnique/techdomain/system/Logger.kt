package ru.fm4m.exercisetechnique.techdomain.system

import java.lang.Exception

interface Logger {
    fun d(tag: String, message: String)
    fun e(tag: String, message: String, e: Throwable? = null)
}