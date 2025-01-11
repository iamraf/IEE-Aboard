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

package gr.ihu.iee.aboard.android.ui.announcements

import android.text.Html
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import gr.ihu.iee.aboard.android.databinding.ItemAnnouncementBinding
import gr.ihu.iee.aboard.android.domain.announcements.entity.Announcement
import gr.ihu.iee.aboard.android.ui.announcements.details.TagsAdapter
import gr.ihu.iee.aboard.android.util.diffutil.ANNOUNCEMENT_DIFF_UTIL

class AnnouncementsPagingAdapter(
    private val listener: AnnouncementsAdapterListener
) : PagingDataAdapter<Announcement, AnnouncementsPagingAdapter.AnnouncementViewHolder>(ANNOUNCEMENT_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAnnouncementBinding.inflate(inflater, parent, false)

        return AnnouncementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class AnnouncementViewHolder(
        private val binding: ItemAnnouncementBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition < 0) return@setOnClickListener

                getItem(bindingAdapterPosition)?.let { item ->
                    listener.onClick(item)
                }
            }
        }

        fun bind(item: Announcement) {
            with(binding) {
                val text = item.body

                pinImg.visibility = if (item.isPinned) VISIBLE else GONE

                bodyTxt.text = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT).toString()

                titleTxt.text = item.title
                authorTxt.text = item.author
                dateTxt.text = item.updatedAt

                val tagsAdapter = TagsAdapter()

                tagsRecycler.apply {
                    adapter = tagsAdapter
                }

                val tags = item.tags.filter { it.id.toInt() != 1 }
                val list = mutableListOf<String>()

                if (tags.isNotEmpty()) {
                    list.add(tags[0].title)

                    if (item.tags.size > 1) {
                        list.add("+${item.tags.size - 1}")
                    }
                }

                tagsAdapter.submitList(list)
            }
        }
    }

    interface AnnouncementsAdapterListener {

        fun onClick(item: Announcement)
    }
}