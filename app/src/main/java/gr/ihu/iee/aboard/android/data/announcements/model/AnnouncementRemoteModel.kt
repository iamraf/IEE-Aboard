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

package gr.ihu.iee.aboard.android.data.announcements.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import gr.ihu.iee.aboard.android.data.tags.model.RemoteTag

@Keep
data class RemoteAnnouncementsResponse(
    val data: List<RemoteAnnouncement?>?
)

@Keep
data class RemoteAnnouncementResponse(
    val data: RemoteAnnouncement?
)

@Keep
data class RemoteAnnouncement(
    val id: String?,
    val title: String?,
    val body: String?,
    val author: RemoteAuthor?,
    val attachments: List<RemoteAttachment?>?,
    var tags: List<RemoteTag?>?,
    @field:Json(name = "created_at") val createdAt: String?,
    @field:Json(name = "updated_at") val updatedAt: String?,
    @field:Json(name = "event_end_time") val eventEndTime: String?,
    @field:Json(name = "event_location") val eventLocation: String?,
    @field:Json(name = "event_start_time") val eventStartTime: String?,
    @field:Json(name = "is_event") val isEvent: Long?,
    @field:Json(name = "is_pinned") val isPinned: Long?,
    @field:Json(name = "pinned_until") val pinnedUntil: String?
)

@Keep
data class RemoteAttachment(
    val id: String?,
    @field:Json(name = "announcement_id") val announcementId: String?,
    @field:Json(name = "filename") val fileName: String?,
    @field:Json(name = "mime_type") val mimeType: String?,
    @field:Json(name = "file_size") val fileSize: Long?,
    @field:Json(name = "created_at") val createdAt: String?,
    @field:Json(name = "updated_at") val updatedAt: String?,
    @field:Json(name = "deleted_at") val deletedAt: String?,
)

@Keep
data class RemoteAuthor(
    val id: String?,
    val name: String?
)