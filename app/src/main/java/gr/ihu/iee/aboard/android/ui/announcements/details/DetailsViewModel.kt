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

package gr.ihu.iee.aboard.android.ui.announcements.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.ihu.iee.aboard.android.base.BaseViewModel
import gr.ihu.iee.aboard.android.domain.announcements.entity.Announcement
import gr.ihu.iee.aboard.android.usecase.announcements.FetchAnnouncementUseCase
import gr.ihu.iee.aboard.android.util.extentions.ResponseException
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val fetchAnnouncementUseCase: FetchAnnouncementUseCase
) : BaseViewModel() {

    private val _announcement = MutableLiveData<Announcement>()
    val announcement: LiveData<Announcement> = _announcement

    fun fetchAnnouncement(id: String) {
        launch {
            setLoading(true)

            delay(500)

            try {
                val announcement = fetchAnnouncementUseCase(id)
                _announcement.value = announcement

                sendNotification(announcement)
            } catch (exception: ResponseException) {
                setResponseError(exception)
            } finally {
                setLoading(false)
            }
        }
    }

    private fun sendNotification(announcement: Announcement) {
        val map: MutableMap<String, String> = HashMap()
        map["title"] = announcement.title
        map["author"] = announcement.author
        map["tag"] = if (announcement.tags.isNotEmpty()) announcement.tags[0].title else "-"
        map["date"] = announcement.updatedAt

        var condition = "'1' in topics"

        announcement.tags
            .filter { it.id.toInt() != 1 }
            .forEach {
                condition += " || '${it.id}' in topics"
            }

        map["condition"] = condition

        FirebaseDatabase.getInstance().reference.child("notifications").child(announcement.id).setValue(map)
    }
}