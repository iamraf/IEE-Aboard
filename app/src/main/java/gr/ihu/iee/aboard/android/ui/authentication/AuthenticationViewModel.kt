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

package gr.ihu.iee.aboard.android.ui.authentication

import androidx.lifecycle.LiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.ihu.iee.aboard.android.base.BaseViewModel
import gr.ihu.iee.aboard.android.usecase.auth.AuthenticateUseCase
import gr.ihu.iee.aboard.android.util.SingleLiveEvent
import gr.ihu.iee.aboard.android.util.extentions.ResponseException
import gr.ihu.iee.aboard.android.util.extentions.UNKNOWN_ERROR_CODE
import gr.ihu.iee.aboard.android.util.manager.PreferencesManager
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authenticateUseCase: AuthenticateUseCase,
    private val preferencesManager: PreferencesManager
) : BaseViewModel() {

    private val _success = SingleLiveEvent<Unit>()
    val success: LiveData<Unit> = _success

    fun authenticate(code: String) {
        launch {
            setLoading(true)

            try {
                val response = authenticateUseCase(code)

                if (response.accessToken != "-" && response.refreshToken != "-") {
                    preferencesManager.accessToken = response.accessToken
                    preferencesManager.refreshToken = response.refreshToken

                    _success.value = Unit
                } else {
                    setResponseError(ResponseException(UNKNOWN_ERROR_CODE, ""))
                }
            } catch (exception: ResponseException) {
                setResponseError(exception)
            } finally {
                setLoading(false)
            }
        }
    }
}