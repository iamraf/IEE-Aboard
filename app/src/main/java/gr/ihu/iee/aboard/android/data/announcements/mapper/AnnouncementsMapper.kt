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

package gr.ihu.iee.aboard.android.data.announcements.mapper

import gr.ihu.iee.aboard.android.data.announcements.model.RemoteAnnouncement
import gr.ihu.iee.aboard.android.data.announcements.model.RemoteAttachment
import gr.ihu.iee.aboard.android.data.tags.mapper.toTag
import gr.ihu.iee.aboard.android.domain.announcements.entity.Announcement
import gr.ihu.iee.aboard.android.domain.announcements.entity.Attachment
import gr.ihu.iee.aboard.android.util.date.DateUtils

fun RemoteAnnouncement.toAnnouncement(): Announcement =
    Announcement(
        id = id ?: "-",
        title = title ?: "-",
        body = body ?: "-",
        author = author?.name ?: "-",
        attachments = attachments?.mapNotNull { attachment ->
            attachment?.toAttachment()
        } ?: emptyList(),
        tags = tags?.mapNotNull { tag ->
            tag?.toTag()
        } ?: emptyList(),
        updatedAt = updatedAt ?: "-",
        isPinned = isPinned != null && isPinned == 1L && pinnedUntil != null && DateUtils.isDateValid(pinnedUntil)
    )

fun RemoteAttachment.toAttachment(): Attachment =
    Attachment(
        id = id ?: "-",
        announcementId = announcementId ?: "-",
        fileName = fileName ?: "-",
        url = attachmentUrl ?: "-"
    )