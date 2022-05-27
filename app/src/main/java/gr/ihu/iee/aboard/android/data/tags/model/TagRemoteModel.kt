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

package gr.ihu.iee.aboard.android.data.tags.model

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class RemoteTagsResponse(
    val data: List<RemoteTag?>?
)

@Keep
data class TagsBody(
    val tags: String
)

@Keep
data class RemoteTag(
    val id: String?,
    val title: String?,
    @field:Json(name = "is_public") val isPublic: Boolean?,
    @field:Json(name = "parent_id") val parentId: String?
)