package ru.fm4m.exercisetechnique.trainingdata.server

import ru.fm4m.exercisetechnique.trainingdomain.repository.AccountRepository
import javax.inject.Inject

public class AccountRepositoryImpl @Inject constructor() : AccountRepository {

    override suspend fun getUserId(): Int {
        return 1
    }
}