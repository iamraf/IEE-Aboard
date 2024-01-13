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

package gr.ihu.iee.aboard.android.ui.announcements

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.ihu.iee.aboard.android.R
import gr.ihu.iee.aboard.android.base.BaseViewModel
import gr.ihu.iee.aboard.android.domain.announcements.entity.Announcement
import gr.ihu.iee.aboard.android.usecase.announcements.FetchAnnouncementsUseCase
import gr.ihu.iee.aboard.android.usecase.notifications.FetchNotificationsUseCase
import gr.ihu.iee.aboard.android.util.AuthenticationLiveData
import gr.ihu.iee.aboard.android.util.SingleLiveEvent
import gr.ihu.iee.aboard.android.util.manager.PreferencesManager
import javax.inject.Inject

@HiltViewModel
class AnnouncementsViewModel @Inject constructor(
    private val fetchAnnouncementsUseCase: FetchAnnouncementsUseCase,
    private val fetchNotificationsUseCase: FetchNotificationsUseCase,
    private val preferencesManager: PreferencesManager
) : BaseViewModel() {

    private val _hasUnread = MutableLiveData<Boolean>()
    val hasUnread: LiveData<Boolean> = _hasUnread

    private val _navigation = SingleLiveEvent<NavDirections>()
    val navigation: LiveData<NavDirections> = _navigation

    val announcements = Pager(
        PagingConfig(pageSize = 10),
        pagingSourceFactory = { AnnouncementsPagingDataSource(fetchAnnouncementsUseCase) }
    ).liveData.cachedIn(viewModelScope)

    fun init() {
        AuthenticationLiveData.postValue(preferencesManager.accessToken != null)

        if (preferencesManager.accessToken != null) {
            fetchNotifications()
        }
    }

    private fun fetchNotifications() {
        launch {
            try {
                val notifications = fetchNotificationsUseCase(1)

                var flag = false

                for (notification in notifications) {
                    if (notification.isUnread) {
                        flag = true
                        break
                    }
                }

                _hasUnread.value = flag
            } catch (e: Exception) {
                // Ignore error
            }
        }
    }

    fun handleMenuClick(id: Int) {
        when (id) {
            R.id.search -> _navigation.value = AnnouncementsFragmentDirections.actionAnnouncementsFragmentToSearchFragment()
            R.id.notifications -> _navigation.value = AnnouncementsFragmentDirections.actionAnnouncementsFragmentToNotificationsFragment()
            R.id.settings -> _navigation.value = AnnouncementsFragmentDirections.actionAnnouncementsFragmentToSettingsFragment()
            R.id.login -> _navigation.value = AnnouncementsFragmentDirections.actionAnnouncementsFragmentToAuthenticationFragment()
        }
    }

    fun handleAnnouncementClick(announcement: Announcement) {
        _navigation.value = AnnouncementsFragmentDirections.actionAnnouncementsFragmentToDetailsFragment(announcement.id, announcement.title)
    }
}
