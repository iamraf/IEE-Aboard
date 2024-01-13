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

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import gr.ihu.iee.aboard.android.R
import gr.ihu.iee.aboard.android.base.BaseFragment
import gr.ihu.iee.aboard.android.databinding.FragmentAnnouncementsBinding
import gr.ihu.iee.aboard.android.domain.announcements.entity.Announcement
import gr.ihu.iee.aboard.android.util.AuthenticationLiveData
import gr.ihu.iee.aboard.android.util.extentions.ResponseException
import gr.ihu.iee.aboard.android.util.extentions.getMessage
import gr.ihu.iee.aboard.android.util.extentions.safeNavigate
import gr.ihu.iee.aboard.android.util.helper.ThemeHelper.hasLightMode
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator

@AndroidEntryPoint
class AnnouncementsFragment : BaseFragment<FragmentAnnouncementsBinding>(), MenuProvider, AnnouncementsPagingAdapter.AnnouncementsAdapterListener {

    override fun initViewBinding(): FragmentAnnouncementsBinding = FragmentAnnouncementsBinding.inflate(layoutInflater)

    private val viewModel: AnnouncementsViewModel by viewModels()

    private val announcementsAdapter: AnnouncementsPagingAdapter by lazy { AnnouncementsPagingAdapter(this) }

    private var isLoggedIn: Boolean? = null
    private var hasUnreadNotifications: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        setupViews()
        setupObservers()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_announcements, menu)

        isLoggedIn?.let {
            val search = menu.findItem(R.id.search)
            search.isVisible = true

            val notifications = menu.findItem(R.id.notifications)
            val icon = if (hasUnreadNotifications) R.drawable.ic_baseline_notifications_24 else R.drawable.ic_baseline_notifications_none_24
            notifications.setIcon(icon)
            notifications.isVisible = it

            val settings = menu.findItem(R.id.settings)
            settings.isVisible = it

            val login = menu.findItem(R.id.login)
            login.isVisible = !it
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        viewModel.handleMenuClick(menuItem.itemId)
        return true
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
                    swipeLayout.isRefreshing = false

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

            swipeLayout.apply {
                if (hasLightMode(resources)) {
                    setColorSchemeColors(Color.BLACK)
                    setProgressBackgroundColorSchemeColor(Color.WHITE)
                } else {
                    setColorSchemeColors(Color.WHITE)
                    setProgressBackgroundColorSchemeColor(Color.BLACK)
                }

                setOnRefreshListener {
                    announcementsAdapter.refresh()
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

            hasUnread.observe(viewLifecycleOwner) {
                hasUnreadNotifications = it
                activity?.invalidateOptionsMenu()
            }

            navigation.observe(viewLifecycleOwner) {
                findNavController().safeNavigate(R.id.announcementsFragment, it)
            }

            AuthenticationLiveData.observe(viewLifecycleOwner) {
                isLoggedIn = it
                activity?.invalidateOptionsMenu()
            }

            init()
        }
    }

    override fun onClick(item: Announcement) {
        viewModel.handleAnnouncementClick(item)
    }
}