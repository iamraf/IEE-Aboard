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

package gr.ihu.iee.aboard.android.data.announcements.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import gr.ihu.iee.aboard.android.data.tags.model.RemoteTag

@Keep
@JsonClass(generateAdapter = true)
data class RemoteAnnouncementsResponse(
    val data: List<RemoteAnnouncement?>?
)

@Keep
@JsonClass(generateAdapter = true)
data class RemoteAnnouncementResponse(
    val data: RemoteAnnouncement?
)

@Keep
@JsonClass(generateAdapter = true)
data class RemoteAnnouncement(
    val id: String?,
    val title: String?,
    val body: String?,
    val author: RemoteAuthor?,
    val attachments: List<RemoteAttachment?>?,
    var tags: List<RemoteTag?>?,
    @Json(name = "created_at") val createdAt: String?,
    @Json(name = "updated_at") val updatedAt: String?,
    @Json(name = "event_end_time") val eventEndTime: String?,
    @Json(name = "event_location") val eventLocation: String?,
    @Json(name = "event_start_time") val eventStartTime: String?,
    @Json(name = "is_event") val isEvent: Long?,
    @Json(name = "is_pinned") val isPinned: Long?,
    @Json(name = "pinned_until") val pinnedUntil: String?
)

@Keep
@JsonClass(generateAdapter = true)
data class RemoteAttachment(
    val id: String?,
    @Json(name = "announcement_id") val announcementId: String?,
    @Json(name = "filename") val fileName: String?,
    @Json(name = "filesize") val fileSize: Long?,
    @Json(name = "mime_type") val mimeType: String?,
    @Json(name = "attachment_url") val attachmentUrl: String?,
)

@Keep
@JsonClass(generateAdapter = true)
data class RemoteAuthor(
    val id: String?,
    val name: String?
)