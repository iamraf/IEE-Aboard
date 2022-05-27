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

package gr.ihu.iee.aboard.android.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import gr.ihu.iee.aboard.android.R
import gr.ihu.iee.aboard.android.databinding.ItemNotificationBinding
import gr.ihu.iee.aboard.android.domain.notifications.entity.Notification
import gr.ihu.iee.aboard.android.util.diffutil.NOTIFICATION_DIFF_UTIL

class NotificationsPagingAdapter(
    private val listener: NotificationsAdapterListener
) : PagingDataAdapter<Notification, NotificationsPagingAdapter.NotificationViewHolder>(NOTIFICATION_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNotificationBinding.inflate(inflater, parent, false)

        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class NotificationViewHolder(private val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Notification) {
            with(binding) {
                if (item.data.type == "announcement.created") {
                    notificationTxt.text = binding.root.context.getString(R.string.notification_announcement, item.data.user, item.data.title)

                    root.setOnClickListener {
                        listener.onClick(item.data.id, item.data.title)
                    }
                } else {
                    notificationTxt.text = root.context.getString(R.string.login)
                    root.setOnClickListener(null)
                }

                if (item.isUnread) {
                    newTxt.visibility = View.VISIBLE
                } else {
                    newTxt.visibility = View.GONE
                }

                dateTxt.text = item.createdAt
            }
        }
    }

    interface NotificationsAdapterListener {
        fun onClick(announcementId: String, title: String)
    }
}