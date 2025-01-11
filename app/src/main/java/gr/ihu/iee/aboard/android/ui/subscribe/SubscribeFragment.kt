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

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import gr.ihu.iee.aboard.android.R
import gr.ihu.iee.aboard.android.base.BaseFragment
import gr.ihu.iee.aboard.android.databinding.FragmentSubscribeBinding
import gr.ihu.iee.aboard.android.domain.tags.entity.Tag
import gr.ihu.iee.aboard.android.util.extentions.getMessage
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator

@AndroidEntryPoint
class SubscribeFragment : BaseFragment<FragmentSubscribeBinding>(), MenuProvider, SubscribeAdapter.SubscribeAdapterListener {

    override fun initViewBinding(): FragmentSubscribeBinding = FragmentSubscribeBinding.inflate(layoutInflater)

    private val viewModel: SubscribeViewModel by viewModels()

    private val subscribeAdapter: SubscribeAdapter by lazy { SubscribeAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        setupViews()
        setupObservers()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_tags, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.apply -> {
                viewModel.updateTags()
                true
            }
            else -> false
        }
    }

    private fun setupViews() {
        with(binding) {
            tagsRecycler.apply {
                setHasFixedSize(true)
                itemAnimator = FadeInDownAnimator()
                adapter = subscribeAdapter
            }
        }
    }

    private fun setupObservers() {
        with(viewModel) {
            tags.observe(viewLifecycleOwner, ::updateTags)

            isLoading.observe(viewLifecycleOwner, ::updateProgress)

            responseError.observe(viewLifecycleOwner) {
                showError(it.getMessage(requireContext()))
            }

            isSuccess.observe(viewLifecycleOwner) {
                showToastError(getString(R.string.changes_made_successfully))
                findNavController().popBackStack()
            }

            isUploading.observe(viewLifecycleOwner) {
                binding.progressBar.isVisible = it
            }

            errorToast.observe(viewLifecycleOwner, ::showToastError)

            fetchTags()
        }
    }

    private fun updateTags(tags: List<Tag>) {
        with(binding) {
            subscribeAdapter.submitList(tags)

            tagsRecycler.isVisible = true
        }
    }

    private fun updateProgress(flag: Boolean) {
        with(binding) {
            when (flag) {
                true -> {
                    progressBar.isVisible = true
                    tagsRecycler.isVisible = false
                    errorTxt.isVisible = false
                }
                false -> progressBar.isVisible = false
            }
        }
    }

    private fun showError(message: String) {
        with(binding) {
            errorTxt.text = message
            errorTxt.isVisible = true
        }
    }

    private fun showToastError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(item: Tag) {
        viewModel.handleTagOnClick(item.id)
    }
}