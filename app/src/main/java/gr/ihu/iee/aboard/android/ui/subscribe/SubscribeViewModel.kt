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

package gr.ihu.iee.aboard.android.ui.subscribe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.ihu.iee.aboard.android.base.BaseViewModel
import gr.ihu.iee.aboard.android.data.tags.model.TagsBody
import gr.ihu.iee.aboard.android.domain.tags.entity.Tag
import gr.ihu.iee.aboard.android.usecase.tags.FetchTagsUseCase
import gr.ihu.iee.aboard.android.usecase.tags.SubscribeTagsUseCase
import gr.ihu.iee.aboard.android.usecase.user.FetchUserUseCase
import gr.ihu.iee.aboard.android.util.SingleLiveEvent
import gr.ihu.iee.aboard.android.util.extentions.ResponseException
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class SubscribeViewModel @Inject constructor(
    private val fetchUserUseCase: FetchUserUseCase,
    private val fetchTagsUseCase: FetchTagsUseCase,
    private val subscribeTagsUseCase: SubscribeTagsUseCase
) : BaseViewModel() {

    private val _tags = MutableLiveData<List<Tag>>()
    val tags: LiveData<List<Tag>> = _tags

    private val _isSuccess = SingleLiveEvent<Unit>()
    val isSuccess: LiveData<Unit> = _isSuccess

    private val _isUploading = SingleLiveEvent<Boolean>()
    val isUploading: LiveData<Boolean> = _isUploading

    private val _errorToast = SingleLiveEvent<String>()
    val errorToast: LiveData<String> = _errorToast

    private val selected = mutableListOf<String>()
    private val removed = mutableListOf<String>()

    fun fetchTags() {
        launch {
            setLoading(true)

            delay(500)

            try {
                val user = fetchUserUseCase()
                val tags = fetchTagsUseCase()

                tags.forEach { tag ->
                    user.subscriptions.forEach { sub ->
                        if (tag.id == sub.id) {
                            selected.add(tag.id)
                            tag.isSelected = true
                        }
                    }
                }

                _tags.value = tags.sortedBy { it.id.toInt() }
            } catch (exception: ResponseException) {
                setResponseError(exception)
            } finally {
                setLoading(false)
            }
        }
    }

    fun updateTags() {
        launch {
            _isUploading.value = true

            try {
                val body = TagsBody(selected.toString())
                subscribeTagsUseCase(body)

                val selectedList = selected.toList()
                selectedList.forEach {
                    FirebaseMessaging.getInstance().subscribeToTopic(it)
                    delay(200)
                }

                val removedList = removed.toList()
                removedList.forEach {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(it)
                    delay(200)
                }

                _isSuccess.value = Unit
            } catch (exception: ResponseException) {
                setResponseError(exception)
            } finally {
                _isUploading.value = false
            }
        }
    }

    fun handleTagOnClick(id: String) {
        if (selected.contains(id)) {
            selected.remove(id)

            if (!removed.contains(id)) {
                removed.add(id)
            }
        } else {
            selected.add(id)

            if (removed.contains(id)) {
                removed.remove(id)
            }
        }
    }
}