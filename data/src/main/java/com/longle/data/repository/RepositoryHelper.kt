package com.longle.data.repository

import com.longle.domain.model.Result
import com.longle.domain.model.StatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 * The function can provide a result backed by the network.
 *
 * @param <RequestType> api type
 */
inline fun <RequestType> networkBoundResult(
    crossinline fetch: suspend () -> Response<RequestType>
) = networkBoundResult(fetch, { null })

/**
 * The function can provide a result backed by the network (with the mapper).
 *
 * @param <Type> domain type
 * @param <RequestType> api type
 */
inline fun <Type, RequestType> networkBoundResult(
    crossinline fetch: suspend () -> Response<RequestType>,
    crossinline mapper: (RequestType?) -> Type?
) = flow {
    emit(Result.loading(null))

    try {
        val response = fetch()
        if (response.isSuccessful) {
            emit(Result.success(response.toStatusCode(), mapper(response.body())))
        } else {
            val errorString = response.errorBody()?.string()
            emit(Result.error(response.toStatusCode(), null, errorString))
        }
    } catch (ex: SSLException) {
        emit(Result.error(StatusCode.UnofficialSslHandshake, null, ex.message))
    } catch (ex: SocketTimeoutException) {
        emit(Result.error(StatusCode.Timeout, null, ex.message))
    } catch (ex: UnknownHostException) {
        emit(Result.error(StatusCode.NoNetwork, null, ex.message))
    } catch (ex: Throwable) {
        emit(Result.error(StatusCode.Unknown, null, ex.message))
    }
}

/**
 * The function can provide a result backed by both the sqlite database and the network.
 *
 *
 * The database serves as the single source of truth.
 * Therefore UI can receive data updates from database only.
 * Function notify UI about:
 * [Result.success] - with data from database
 * [Result.error] - if error has occurred from any source
 * [Result.loading] - start perform
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 * @param <Type> domain type
 * @param <ResultType> database type
 * @param <RequestType> api type
 */
inline fun <Type, ResultType, RequestType> networkBoundResult(
    crossinline query: () -> Flow<ResultType?>,
    crossinline fetch: suspend () -> Response<RequestType>,
    crossinline shouldFetch: (ResultType?) -> Boolean,
    crossinline saveFetchResult: (RequestType) -> Unit,
    crossinline mapper: (ResultType?) -> Type?,
    crossinline onFetchFailed: (Throwable) -> Unit = { }
) = channelFlow {
    send(Result.loading(null))
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        val loadingJob = launch(currentCoroutineContext()) {
            query().collect { send(Result.loading(mapper(it))) }
        }

        try {
            val response = fetch()
            loadingJob.cancel()
            response.body()?.let {
                withContext(Dispatchers.IO) { saveFetchResult(it) }
            }
            if (response.isSuccessful) {
                query().map { Result.success(response.toStatusCode(), mapper(it)) }
            } else {
                val message = response.errorBody()?.string()
                val errorMsg = if (message.isNullOrBlank()) {
                    response.message() ?: "unknown error"
                } else {
                    message
                }
                query().map { Result.error(response.toStatusCode(), mapper(it), errorMsg) }
            }
        } catch (ex: SSLException) {
            loadingJob.cancel()
            onFetchFailed(ex)
            query().map {
                Result.error(
                    StatusCode.UnofficialSslHandshake,
                    mapper(it),
                    ex.message
                )
            }
        } catch (ex: SocketTimeoutException) {
            loadingJob.cancel()
            onFetchFailed(ex)
            query().map { Result.error(StatusCode.Timeout, mapper(it), ex.message) }
        } catch (ex: UnknownHostException) {
            loadingJob.cancel()
            onFetchFailed(ex)
            query().map { Result.error(StatusCode.NoNetwork, mapper(it), ex.message) }
        } catch (ex: Throwable) {
            loadingJob.cancel()
            onFetchFailed(ex)
            query().map { Result.error(StatusCode.Unknown, mapper(it), ex.message) }
        }
    } else {
        query().map { Result.success(StatusCode.Cached, mapper(it)) }
    }

    flow.collect { send(it) }
}.distinctUntilChanged()

fun Response<*>.toStatusCode(): StatusCode {
    return when (code()) {

        HttpURLConnection.HTTP_OK -> StatusCode.Ok

        HttpURLConnection.HTTP_ACCEPTED -> StatusCode.Accepted

        HttpURLConnection.HTTP_NOT_MODIFIED -> StatusCode.NotModified

        HttpURLConnection.HTTP_NO_CONTENT -> StatusCode.NoContent

        HttpURLConnection.HTTP_NOT_FOUND -> StatusCode.NotFound

        HttpURLConnection.HTTP_FORBIDDEN -> StatusCode.Forbidden

        else -> StatusCode.Unknown
    }
}
