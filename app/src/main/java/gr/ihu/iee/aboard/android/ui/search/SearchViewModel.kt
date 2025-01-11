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

package gr.ihu.iee.aboard.android.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.ihu.iee.aboard.android.base.BaseViewModel
import gr.ihu.iee.aboard.android.usecase.announcements.FetchSearchUseCase
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val fetchSearchUseCase: FetchSearchUseCase,
    state: SavedStateHandle
) : BaseViewModel() {

    private val _currentQuery = state.getLiveData("current", "")

    val announcements = _currentQuery.switchMap { queryString ->
        Pager(
            PagingConfig(pageSize = 10),
            pagingSourceFactory = { SearchPagingDataSource(fetchSearchUseCase, queryString) }
        ).liveData.cachedIn(viewModelScope)
    }

    fun search(query: String) {
        _currentQuery.value = query
    }
}