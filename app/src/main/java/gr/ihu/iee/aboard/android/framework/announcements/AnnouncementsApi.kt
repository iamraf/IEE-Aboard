/*
 * Copyright (C) 2020-2024 Raf
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

package gr.ihu.iee.aboard.android.framework.announcements

import gr.ihu.iee.aboard.android.data.announcements.model.RemoteAnnouncementResponse
import gr.ihu.iee.aboard.android.data.announcements.model.RemoteAnnouncementsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AnnouncementsApi {

    @GET("announcements")
    suspend fun fetchPagingAnnouncements(@Query("page") page: Int): Response<RemoteAnnouncementsResponse>

    @GET("announcements")
    suspend fun fetchPagingSearch(
        @Query("page") page: Int,
        @Query("title", encoded = true) query: String
    ): Response<RemoteAnnouncementsResponse>

    @GET("announcements/{id}")
    suspend fun fetchAnnouncement(@Path("id") id: String): Response<RemoteAnnouncementResponse>
}