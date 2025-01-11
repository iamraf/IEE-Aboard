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

package gr.ihu.iee.aboard.android.util.interceptor

import gr.ihu.iee.aboard.android.framework.auth.AuthApi
import gr.ihu.iee.aboard.android.util.API_URL
import gr.ihu.iee.aboard.android.util.AuthenticationLiveData
import gr.ihu.iee.aboard.android.util.manager.PreferencesManager
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val authApi: AuthApi
) : Authenticator {

    @Throws(IOException::class)
    override fun authenticate(route: Route?, response: Response): Request? {
        if (preferencesManager.refreshToken != null) {
            try {
                val authResponse = authApi.refreshToken(code = preferencesManager.refreshToken!!).execute().body()

                if (authResponse?.accessToken != null && authResponse.refreshToken != null) {
                    preferencesManager.accessToken = authResponse.accessToken
                    preferencesManager.refreshToken = authResponse.refreshToken

                    if (AuthenticationLiveData.value != true) {
                        AuthenticationLiveData.postValue(true)
                    }

                    return response.request.newBuilder()
                        .header("Authorization", "Bearer ${authResponse.accessToken}")
                        .build()
                }
            } catch (e: Exception) {
                preferencesManager.accessToken = null
                preferencesManager.refreshToken = null
            }
        }

        if (AuthenticationLiveData.value != false) {
            AuthenticationLiveData.postValue(false)
        }

        preferencesManager.accessToken = null
        preferencesManager.refreshToken = null

        if (response.request.url.toString().startsWith(API_URL + "announcements")) {
            return response.request.newBuilder()
                .removeHeader("Authorization")
                .build()
        }

        return null
    }
}