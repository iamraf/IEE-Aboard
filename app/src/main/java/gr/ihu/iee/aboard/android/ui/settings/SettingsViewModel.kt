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

package gr.ihu.iee.aboard.android.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.ihu.iee.aboard.android.base.BaseViewModel
import gr.ihu.iee.aboard.android.usecase.user.FetchUserUseCase
import gr.ihu.iee.aboard.android.util.SingleLiveEvent
import gr.ihu.iee.aboard.android.util.extentions.ResponseException
import gr.ihu.iee.aboard.android.util.manager.PreferencesManager
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val fetchUserUseCase: FetchUserUseCase,
    private val preferencesManager: PreferencesManager
) : BaseViewModel() {

    private val _areNotificationsEnabled = MutableLiveData<Boolean>()
    val areNotificationsEnabled: LiveData<Boolean> = _areNotificationsEnabled

    private val _logoutSuccessful = SingleLiveEvent<Unit>()
    val logoutSuccessful: LiveData<Unit> = _logoutSuccessful

    init {
        _areNotificationsEnabled.value = preferencesManager.notifications
    }

    fun logout() {
        launch {
            setLoading(true)

            try {
                preferencesManager.accessToken = null
                preferencesManager.refreshToken = null

                _logoutSuccessful.value = Unit
            } catch (exception: ResponseException) {
                setResponseError(exception)
            } finally {
                setLoading(false)
            }
        }
    }

    private fun enableNotifications() {
        launch {
            setLoading(true)

            try {
                val user = fetchUserUseCase()

                user.subscriptions.forEach {
                    FirebaseMessaging.getInstance().subscribeToTopic(it.id)
                    delay(200)
                }

                preferencesManager.notifications = true
                _areNotificationsEnabled.value = true
            } catch (exception: ResponseException) {
                setResponseError(exception)
            } finally {
                setLoading(false)
            }
        }
    }

    fun getTheme(): String {
        return preferencesManager.theme
    }

    fun setTheme(theme: String) {
        preferencesManager.theme = theme
    }

    fun setNotifications() {
        if (!preferencesManager.notifications) {
            enableNotifications()
        } else {
            preferencesManager.notifications = false
            _areNotificationsEnabled.value = false
        }
    }
}