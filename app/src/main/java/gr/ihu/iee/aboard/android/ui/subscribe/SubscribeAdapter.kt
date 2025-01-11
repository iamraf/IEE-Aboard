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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gr.ihu.iee.aboard.android.databinding.ItemSubscribeBinding
import gr.ihu.iee.aboard.android.domain.tags.entity.Tag
import gr.ihu.iee.aboard.android.util.diffutil.TAG_DIFF_UTIL

class SubscribeAdapter(
    private val listener: SubscribeAdapterListener
) : ListAdapter<Tag, SubscribeAdapter.SubscribeViewHolder>(TAG_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscribeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSubscribeBinding.inflate(inflater, parent, false)

        return SubscribeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscribeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SubscribeViewHolder(
        private val binding: ItemSubscribeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition < 0) return@setOnClickListener

                val item = getItem(bindingAdapterPosition)
                listener.onClick(item)
                item.isSelected = !item.isSelected
                binding.checkbox.isChecked = item.isSelected
            }
        }

        fun bind(item: Tag) {
            with(binding) {
                checkbox.isClickable = false
                checkbox.isChecked = item.isSelected
                tagTxt.text = item.title
            }
        }
    }

    interface SubscribeAdapterListener {

        fun onClick(item: Tag)
    }
}