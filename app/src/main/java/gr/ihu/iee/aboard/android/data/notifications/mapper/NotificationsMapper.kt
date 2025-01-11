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

package gr.ihu.iee.aboard.android.data.notifications.mapper

import gr.ihu.iee.aboard.android.data.notifications.model.RemoteNotification
import gr.ihu.iee.aboard.android.data.notifications.model.RemoteNotificationData
import gr.ihu.iee.aboard.android.domain.notifications.entity.Notification
import gr.ihu.iee.aboard.android.domain.notifications.entity.NotificationData

fun RemoteNotification.toNotification(): Notification =
    Notification(
        id = id ?: "-",
        createdAt = createdAt ?: "-",
        isUnread = readAt == null,
        data = data?.toNotificationData() ?: NotificationData("-", "-", "-", "-")
    )

fun RemoteNotificationData.toNotificationData(): NotificationData =
    NotificationData(
        id = id ?: "-",
        type = type ?: "-",
        user = user ?: "-",
        title = title ?: "-"
    )