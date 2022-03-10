package com.longle.domain.util

import com.longle.domain.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <Status, UseCaseResult, Data> Result<Status, Data>.toUseCaseResult(
    useCaseResult: UseCaseResult
): Result<UseCaseResult, Data> {
    return Result(useCaseResult, data)
}

fun <State, Data> Result<State, Data>.asFlow(): Flow<Result<State, Data>> {
    val result = this
    return flow {
        emit(result)
    }
}
