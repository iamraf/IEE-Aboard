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

package gr.ihu.iee.aboard.android.data.tags

import gr.ihu.iee.aboard.android.data.tags.mapper.toTag
import gr.ihu.iee.aboard.android.data.tags.model.TagsBody
import gr.ihu.iee.aboard.android.domain.tags.TagsDataSource
import gr.ihu.iee.aboard.android.domain.tags.entity.Tag
import javax.inject.Inject

class TagsDataSourceImpl @Inject constructor(
    private val repository: TagsRepository
) : TagsDataSource {

    override suspend fun fetchTags(): List<Tag> {
        val response = repository.fetchTags()

        return response.data?.mapNotNull {
            it?.toTag()
        } ?: emptyList()
    }

    override suspend fun subscribeTags(tagsBody: TagsBody) {
        repository.subscribeTags(tagsBody)
    }
}