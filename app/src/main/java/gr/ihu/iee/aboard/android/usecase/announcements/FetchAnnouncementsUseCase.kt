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

package gr.ihu.iee.aboard.android.usecase.announcements

import gr.ihu.iee.aboard.android.domain.announcements.AnnouncementsDataSource
import gr.ihu.iee.aboard.android.domain.announcements.entity.Announcement
import javax.inject.Inject

class FetchAnnouncementsUseCase @Inject constructor(
    private val dataSource: AnnouncementsDataSource
) {

    suspend operator fun invoke(page: Int): List<Announcement> {
        return dataSource.fetchPagingAnnouncements(page)
    }
}