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

package gr.ihu.iee.aboard.android.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.ihu.iee.aboard.android.util.SingleLiveEvent
import gr.ihu.iee.aboard.android.util.extentions.ResponseException
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _isLoading = SingleLiveEvent<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _responseError = SingleLiveEvent<ResponseException>()
    val responseError: LiveData<ResponseException> = _responseError

    protected fun setLoading(flag: Boolean) {
        _isLoading.value = flag
    }

    protected fun setResponseError(responseException: ResponseException) {
        _responseError.value = responseException
    }

    protected fun launch(function: suspend () -> Unit) {
        viewModelScope.launch {
            function.invoke()
        }
    }
}