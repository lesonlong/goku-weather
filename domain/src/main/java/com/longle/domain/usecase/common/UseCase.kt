package com.longle.domain.usecase.common

import com.longle.domain.model.Result
import kotlinx.coroutines.flow.Flow

/**
 * Base UseCase
 *
 * @param <Param> Parameter that need to be execute UseCase
 * @param <UseCaseResult> Result of UseCase after execution
 * @param <Data> Data of UseCase after execution
 */
interface UseCase<Param, UseCaseResult, Data> {

    fun execute(param: Param): Flow<Result<UseCaseResult, Data>>
}
