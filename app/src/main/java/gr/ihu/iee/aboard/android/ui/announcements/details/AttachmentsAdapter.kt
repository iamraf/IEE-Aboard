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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gr.ihu.iee.aboard.android.databinding.ItemAttachmentBinding
import gr.ihu.iee.aboard.android.domain.announcements.entity.Attachment
import gr.ihu.iee.aboard.android.util.diffutil.ATTACHMENT_DIFF_UTIL

class AttachmentsAdapter(
    private val listener: AttachmentsAdapterListener
) : ListAdapter<Attachment, AttachmentsAdapter.AttachmentViewHolder>(ATTACHMENT_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAttachmentBinding.inflate(inflater, parent, false)

        return AttachmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttachmentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AttachmentViewHolder(private val binding: ItemAttachmentBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition < 0) return@setOnClickListener

                listener.onClick(getItem(bindingAdapterPosition))
            }
        }

        fun bind(item: Attachment) {
            with(binding) {
                attachmentTxt.text = item.fileName
            }
        }
    }

    interface AttachmentsAdapterListener {
        fun onClick(item: Attachment)
    }
}