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

package gr.ihu.iee.aboard.android.domain.announcements.entity

import gr.ihu.iee.aboard.android.domain.tags.entity.Tag

data class Announcement(
    val id: String,
    val title: String,
    val body: String,
    val author: String,
    val attachments: List<Attachment>,
    val tags: List<Tag>,
    val updatedAt: String,
    val isPinned: Boolean
)

data class Attachment(
    val id: String,
    val announcementId: String,
    val fileName: String,
    val url: String
)