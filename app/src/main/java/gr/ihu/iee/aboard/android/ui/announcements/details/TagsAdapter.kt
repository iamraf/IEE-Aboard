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

package gr.ihu.iee.aboard.android.ui.announcements.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gr.ihu.iee.aboard.android.databinding.ItemTagBinding
import gr.ihu.iee.aboard.android.util.diffutil.STRING_DIFF_UTIL

class TagsAdapter : ListAdapter<String, TagsAdapter.TagViewHolder>(STRING_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTagBinding.inflate(inflater, parent, false)

        return TagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TagViewHolder(
        private val binding: ItemTagBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            with(binding) {
                titleTxt.text = item
            }
        }
    }
}