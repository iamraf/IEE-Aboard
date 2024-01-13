/*
 * Copyright (C) 2020-2024 Raf
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

package gr.ihu.iee.aboard.android.framework.notifications

import gr.ihu.iee.aboard.android.data.notifications.NotificationsRepository
import gr.ihu.iee.aboard.android.data.notifications.model.RemoteNotificationsResponse
import gr.ihu.iee.aboard.android.util.extentions.apiCall
import javax.inject.Inject

class NotificationsRepositoryImpl @Inject constructor(
    private val api: NotificationsApi
) : NotificationsRepository {

    override suspend fun fetchPagingNotifications(page: Int): RemoteNotificationsResponse = apiCall {
        api.fetchPagingNotifications(page)
    }

    override suspend fun readNotifications() = apiCall {
        api.readNotifications()
    }
}