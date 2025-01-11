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

package gr.ihu.iee.aboard.android.ui.notifications

import androidx.paging.PagingSource
import androidx.paging.PagingState
import gr.ihu.iee.aboard.android.domain.notifications.entity.Notification
import gr.ihu.iee.aboard.android.usecase.notifications.FetchNotificationsUseCase
import gr.ihu.iee.aboard.android.usecase.notifications.ReadNotificationsUseCase
import gr.ihu.iee.aboard.android.util.extentions.ResponseException
import kotlinx.coroutines.delay

class NotificationsPagingDataSource(
    private val fetchNotificationsUseCase: FetchNotificationsUseCase,
    private val readNotificationsUseCase: ReadNotificationsUseCase
) : PagingSource<Int, Notification>() {

    private val initialPageIndex: Int = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Notification> {
        val position = params.key ?: initialPageIndex

        if (position == initialPageIndex) {
            delay(500)
        }

        return try {
            val response = fetchNotificationsUseCase(position)

            if (position == initialPageIndex) {
                readNotificationsUseCase()
            }

            LoadResult.Page(
                data = response,
                prevKey = if (position == initialPageIndex) null else position - 1,
                nextKey = if (response.isEmpty()) null else position + 1
            )
        } catch (exception: ResponseException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Notification>): Int? {
        return state.anchorPosition
    }
}