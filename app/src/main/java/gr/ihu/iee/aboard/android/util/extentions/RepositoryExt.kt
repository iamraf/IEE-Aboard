/*
 * Copyright (C) 2020-2025 Raf
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package gr.ihu.iee.aboard.android.util.extentions

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import gr.ihu.iee.aboard.android.data.auth.model.ErrorBody
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun <T : Any> Response<T>.handleApiResponse(): T {
    val body = body()
    return if (isSuccessful && body != null) {
        body
    } else {
        var error: String? = null
        val errorBody = errorBody()?.string()

        if (errorBody != null) {
            error = try {
                val jsonAdapter: JsonAdapter<ErrorBody> = Moshi.Builder().build().adapter(ErrorBody::class.java)
                jsonAdapter.fromJson(errorBody)?.error
            } catch (e: Exception) {
                null
            }
        }

        throw ResponseException(code(), error ?: "")
    }
}

suspend fun <R : Any> apiCall(function: suspend () -> Response<R>): R {
    return withContext(IO) {
        try {
            function.invoke().handleApiResponse()
        } catch (e: Exception) {
            var error = ""
            val code = when (e) {
                is IOException -> {
                    when (e) {
                        is UnknownHostException -> NO_INTERNET_CONNECTION_CODE
                        is SocketTimeoutException -> TIME_OUT_CODE
                        else -> UNKNOWN_ERROR_CODE
                    }
                }
                is ResponseException -> {
                    error = e.error
                    if (e.code == 401) {
                        REQUEST_UNAUTHENTICATED_CODE
                    } else {
                        REQUEST_ERROR_CODE
                    }
                }
                else -> UNKNOWN_ERROR_CODE
            }

            throw ResponseException(code, error)
        }
    }
}