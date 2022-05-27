/*
 * Copyright (C) 2020-2022 Raf
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

package gr.ihu.iee.aboard.android.data.user.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import gr.ihu.iee.aboard.android.data.tags.model.RemoteTag

@Keep
data class RemoteUserResponse(
    val data: RemoteUser?
)

@Keep
data class RemoteUser(
    val id: String?,
    val email: String?,
    @field:Json(name = "is_admin") val isAdmin: Long?,
    @field:Json(name = "is_author") val isAuthor: Long?,
    @field:Json(name = "last_interaction_time") val lastInteractionTime: String?,
    @field:Json(name = "last_login_at") val lastLoginAt: String?,
    val name: String?,
    val subscriptions: List<RemoteTag?>?,
    val uid: String?
)