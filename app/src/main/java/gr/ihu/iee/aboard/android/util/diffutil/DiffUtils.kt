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

package gr.ihu.iee.aboard.android.util.diffutil

import androidx.recyclerview.widget.DiffUtil
import gr.ihu.iee.aboard.android.domain.announcements.entity.Announcement
import gr.ihu.iee.aboard.android.domain.announcements.entity.Attachment
import gr.ihu.iee.aboard.android.domain.notifications.entity.Notification
import gr.ihu.iee.aboard.android.domain.tags.entity.Tag

val ANNOUNCEMENT_DIFF_UTIL = object : DiffUtil.ItemCallback<Announcement>() {
    override fun areItemsTheSame(oldItem: Announcement, newItem: Announcement): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Announcement, newItem: Announcement): Boolean = oldItem == newItem
}

val ATTACHMENT_DIFF_UTIL = object : DiffUtil.ItemCallback<Attachment>() {
    override fun areItemsTheSame(oldItem: Attachment, newItem: Attachment): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Attachment, newItem: Attachment): Boolean = oldItem == newItem
}

val NOTIFICATION_DIFF_UTIL = object : DiffUtil.ItemCallback<Notification>() {
    override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean = oldItem == newItem
}

val TAG_DIFF_UTIL = object : DiffUtil.ItemCallback<Tag>() {
    override fun areItemsTheSame(oldItem: Tag, newItem: Tag): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Tag, newItem: Tag): Boolean = oldItem == newItem
}

val STRING_DIFF_UTIL = object : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
}