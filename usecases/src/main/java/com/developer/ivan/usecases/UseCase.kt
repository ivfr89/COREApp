package com.developer.ivan.usecases

import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

abstract class UseCase<Params, Return> {

    suspend operator fun invoke(params: Params) : Flow<Either<Failure, Return>> {
        return execute(params).flowOn(Dispatchers.IO)
    }

    protected abstract suspend fun execute(params: Params): Flow<Either<Failure, Return>>
}