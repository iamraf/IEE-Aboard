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

package gr.ihu.iee.aboard.android.ui.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import dagger.hilt.android.AndroidEntryPoint
import gr.ihu.iee.aboard.android.R
import gr.ihu.iee.aboard.android.base.BaseMenuFragment
import gr.ihu.iee.aboard.android.databinding.FragmentSearchBinding
import gr.ihu.iee.aboard.android.domain.announcements.entity.Announcement
import gr.ihu.iee.aboard.android.ui.announcements.AnnouncementsPagingAdapter
import gr.ihu.iee.aboard.android.ui.announcements.AnnouncementsPagingStateAdapter
import gr.ihu.iee.aboard.android.util.extentions.ResponseException
import gr.ihu.iee.aboard.android.util.extentions.getMessage
import gr.ihu.iee.aboard.android.util.extentions.safeNavigate
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator

@AndroidEntryPoint
class SearchFragment : BaseMenuFragment<FragmentSearchBinding>(), AnnouncementsPagingAdapter.AnnouncementsAdapterListener {

    override fun initViewBinding(): FragmentSearchBinding = FragmentSearchBinding.inflate(layoutInflater)

    private val viewModel: SearchViewModel by viewModels()

    private val announcementsAdapter: AnnouncementsPagingAdapter by lazy { AnnouncementsPagingAdapter(this) }

    private var currentQuery: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)

        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.apply {
            queryHint = getString(R.string.search_hint)
            maxWidth = Int.MAX_VALUE
            isIconified = false
            setQuery(currentQuery ?: "", false)

            setOnCloseListener {
                findNavController().navigateUp()
            }

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    currentQuery = query
                    announcementsAdapter.submitData(lifecycle, PagingData.empty())
                    viewModel.search(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            })
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupViews() {
        with(binding) {
            announcementsAdapter.addLoadStateListener { loadState ->
                if (loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading) {
                    if (announcementsAdapter.itemCount == 0) {
                        progressBar.isVisible = true
                    }

                    errorTxt.isVisible = false
                } else {
                    progressBar.isVisible = false

                    val errorState = when {
                        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                        else -> null
                    }

                    errorState?.let {
                        if (announcementsAdapter.itemCount == 0) {
                            announcementsRecycler.isVisible = false

                            errorTxt.isVisible = true
                            errorTxt.text = (it.error as ResponseException).getMessage(requireContext())
                        }
                    }
                }
            }

            announcementsRecycler.apply {
                setHasFixedSize(true)
                itemAnimator = FadeInDownAnimator()
                adapter = announcementsAdapter.withLoadStateFooter(AnnouncementsPagingStateAdapter(announcementsAdapter::retry))
            }
        }
    }

    private fun setupObservers() {
        with(viewModel) {
            announcements.observe(viewLifecycleOwner) {
                announcementsAdapter.submitData(viewLifecycleOwner.lifecycle, it)

                binding.announcementsRecycler.isVisible = true
                binding.errorTxt.isVisible = false
            }
        }
    }

    override fun onClick(item: Announcement) {
        val title = if (item.tags.isNotEmpty()) item.tags[0].title else item.title
        val action = SearchFragmentDirections.actionSearchFragmentToDetailsFragment(item.id, title)
        findNavController().safeNavigate(R.id.searchFragment, action)
    }
}