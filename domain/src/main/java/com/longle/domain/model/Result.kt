package com.longle.domain.model

import com.longle.domain.model.Status.*

/**
 * A generic class that holds a value with its state.
 * @param <Data>
 * @param <State>
 */
data class Result<out State, out Data>(val state: State, val data: Data?) {
    companion object {

        fun <Data> loading(data: Data?): Result<Status, Data> {
            return Result(Loading(), data)
        }

        fun <Data> success(code: StatusCode, data: Data?): Result<Status, Data> {
            return Result(Success(code), data)
        }

        fun <Data> error(code: StatusCode, data: Data?, message: String?): Result<Status, Data> {
            return Result(Error(code, message), data)
        }
    }
}
