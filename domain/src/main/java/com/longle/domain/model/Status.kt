package com.longle.domain.model

/**
 * Raw domain status of a result that is provided by the Data module.
 * These are usually created by the Repository classes where they return
 */
sealed class Status(open val code: StatusCode, open val message: String?) {

    class Loading : Status(StatusCode.Loading, null)

    class Success(override val code: StatusCode) : Status(code, null)

    class Error(override val code: StatusCode, override val message: String?) :
        Status(code, message)

    override fun equals(other: Any?): Boolean {
        return if (other is Status) {
            this.code == other.code &&
                this.message == other.message
        } else false
    }
}

enum class StatusCode {
    Loading,
    Cached,
    NoNetwork,
    Ok,
    Accepted,
    NotModified,
    NoContent,
    NotFound,
    UnofficialSslHandshake,
    Forbidden,
    Timeout,
    Unknown
}
