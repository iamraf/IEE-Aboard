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

package gr.ihu.iee.aboard.android.data.notifications.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class RemoteNotificationsResponse(
    val data: List<RemoteNotification?>?
)

@Keep
@JsonClass(generateAdapter = true)
data class RemoteNotification(
    val id: String?,
    @Json(name = "created_at") val createdAt: String?,
    @Json(name = "read_at") val readAt: String?,
    val data: RemoteNotificationData?
)

@Keep
@JsonClass(generateAdapter = true)
data class RemoteNotificationData(
    val id: String?,
    val type: String?,
    val title: String?,
    val user: String?
)