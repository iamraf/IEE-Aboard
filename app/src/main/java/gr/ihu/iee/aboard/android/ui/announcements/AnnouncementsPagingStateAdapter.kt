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

package gr.ihu.iee.aboard.android.ui.announcements

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import gr.ihu.iee.aboard.android.databinding.ItemFooterBinding

class AnnouncementsPagingStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<AnnouncementsPagingStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFooterBinding.inflate(inflater, parent, false)

        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadStateViewHolder(private val binding: ItemFooterBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.retryBtn.setOnClickListener {
                if (bindingAdapterPosition < 0) return@setOnClickListener

                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.errorTxt.isVisible = loadState !is LoadState.Loading
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryBtn.isVisible = loadState !is LoadState.Loading

            if (loadState is LoadState.Error) {
                binding.errorTxt.text = loadState.error.message
            }
        }
    }
}