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

package gr.ihu.iee.aboard.android.framework.auth

import gr.ihu.iee.aboard.android.data.auth.model.RemoteAuthResponse
import gr.ihu.iee.aboard.android.util.CLIENT_ID
import gr.ihu.iee.aboard.android.util.CLIENT_SECRET
import gr.ihu.iee.aboard.android.util.GRAND_TYPE_AUTHORIZATION
import gr.ihu.iee.aboard.android.util.GRAND_TYPE_REFRESH_TOKEN
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApi {

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("token")
    suspend fun authenticate(
        @Field("client_id") clientId: String = CLIENT_ID,
        @Field("client_secret") clientSecret: String = CLIENT_SECRET,
        @Field("grant_type") grantType: String = GRAND_TYPE_AUTHORIZATION,
        @Field("code") code: String
    ): Response<RemoteAuthResponse>

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("token")
    fun refreshToken(
        @Field("client_id") clientId: String = CLIENT_ID,
        @Field("client_secret") clientSecret: String = CLIENT_SECRET,
        @Field("grant_type") grantType: String = GRAND_TYPE_REFRESH_TOKEN,
        @Field("code") code: String
    ): Call<RemoteAuthResponse>
}