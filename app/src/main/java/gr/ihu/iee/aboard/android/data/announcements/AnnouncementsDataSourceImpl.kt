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

package gr.ihu.iee.aboard.android.data.announcements

import gr.ihu.iee.aboard.android.data.announcements.mapper.toAnnouncement
import gr.ihu.iee.aboard.android.domain.announcements.AnnouncementsDataSource
import gr.ihu.iee.aboard.android.domain.announcements.entity.Announcement
import javax.inject.Inject

class AnnouncementsDataSourceImpl @Inject constructor(
    private val repository: AnnouncementsRepository
) : AnnouncementsDataSource {

    override suspend fun fetchPagingAnnouncements(page: Int): List<Announcement> {
        val response = repository.fetchPagingAnnouncements(page)

        return response.data?.mapNotNull {
            it?.toAnnouncement()
        } ?: emptyList()
    }

    override suspend fun fetchPagingSearch(page: Int, query: String): List<Announcement> {
        val response = repository.fetchPagingSearch(page, query)

        return response.data?.mapNotNull {
            it?.toAnnouncement()
        } ?: emptyList()
    }

    override suspend fun fetchAnnouncement(id: String): Announcement {
        val response = repository.fetchAnnouncement(id)

        return response.data?.toAnnouncement() ?: throw Exception()
    }
}