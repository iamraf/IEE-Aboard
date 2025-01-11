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

package gr.ihu.iee.aboard.android.ui.announcements

import androidx.paging.PagingSource
import androidx.paging.PagingState
import gr.ihu.iee.aboard.android.domain.announcements.entity.Announcement
import gr.ihu.iee.aboard.android.usecase.announcements.FetchAnnouncementsUseCase
import gr.ihu.iee.aboard.android.util.extentions.ResponseException

class AnnouncementsPagingDataSource(
    private val fetchAnnouncementsUseCase: FetchAnnouncementsUseCase
) : PagingSource<Int, Announcement>() {

    private val initialPageIndex: Int = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Announcement> {
        val position = params.key ?: initialPageIndex

        return try {
            val response = fetchAnnouncementsUseCase(position)

            LoadResult.Page(
                data = response,
                prevKey = if (position == initialPageIndex) null else position - 1,
                nextKey = if (response.isEmpty()) null else position + 1
            )
        } catch (exception: ResponseException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Announcement>): Int = initialPageIndex
}