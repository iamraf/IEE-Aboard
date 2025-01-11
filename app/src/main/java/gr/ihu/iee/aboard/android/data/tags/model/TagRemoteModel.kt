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

package gr.ihu.iee.aboard.android.data.tags.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class RemoteTagsResponse(
    val data: List<RemoteTag?>?
)

@Keep
@JsonClass(generateAdapter = true)
data class TagsBody(
    val tags: String
)

@Keep
@JsonClass(generateAdapter = true)
data class RemoteTag(
    val id: String?,
    val title: String?,
    @Json(name = "is_public") val isPublic: Boolean?,
    @Json(name = "parent_id") val parentId: String?
)