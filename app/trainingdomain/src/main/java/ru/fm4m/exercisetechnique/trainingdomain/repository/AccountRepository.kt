package ru.fm4m.exercisetechnique.trainingdomain.repository

interface AccountRepository {

    suspend fun getUserId() : Int
}