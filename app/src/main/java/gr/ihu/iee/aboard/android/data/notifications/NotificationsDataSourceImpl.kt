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

package gr.ihu.iee.aboard.android.data.notifications

import gr.ihu.iee.aboard.android.data.notifications.mapper.toNotification
import gr.ihu.iee.aboard.android.domain.notifications.NotificationsDataSource
import gr.ihu.iee.aboard.android.domain.notifications.entity.Notification
import javax.inject.Inject

class NotificationsDataSourceImpl @Inject constructor(
    private val repository: NotificationsRepository
) : NotificationsDataSource {

    override suspend fun fetchPagingNotifications(page: Int): List<Notification> {
        val response = repository.fetchPagingNotifications(page)

        return response.data?.mapNotNull {
            it?.toNotification()
        } ?: emptyList()
    }

    override suspend fun readNotifications() {
        repository.readNotifications()
    }
}