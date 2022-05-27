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

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import gr.ihu.iee.aboard.android.R
import gr.ihu.iee.aboard.android.base.BaseFragment
import gr.ihu.iee.aboard.android.databinding.FragmentNotificationsBinding
import gr.ihu.iee.aboard.android.ui.announcements.AnnouncementsPagingStateAdapter
import gr.ihu.iee.aboard.android.util.extentions.ResponseException
import gr.ihu.iee.aboard.android.util.extentions.getMessage
import gr.ihu.iee.aboard.android.util.extentions.safeNavigate
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator

@AndroidEntryPoint
class NotificationsFragment : BaseFragment<FragmentNotificationsBinding>(), NotificationsPagingAdapter.NotificationsAdapterListener {

    override fun initViewBinding(): FragmentNotificationsBinding = FragmentNotificationsBinding.inflate(layoutInflater)

    private val viewModel: NotificationsViewModel by viewModels()

    private val notificationsAdapter: NotificationsPagingAdapter by lazy { NotificationsPagingAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        with(binding) {
            notificationsAdapter.addLoadStateListener { loadState ->
                if (loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading) {
                    if (notificationsAdapter.itemCount == 0) {
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
                        if (notificationsAdapter.itemCount == 0) {
                            notificationsRecycler.isVisible = false

                            errorTxt.isVisible = true
                            errorTxt.text = (it.error as ResponseException).getMessage(requireContext())
                        }
                    }
                }
            }

            notificationsRecycler.apply {
                setHasFixedSize(true)
                itemAnimator = FadeInDownAnimator()
                adapter = notificationsAdapter.withLoadStateFooter(AnnouncementsPagingStateAdapter(notificationsAdapter::retry))
            }
        }
    }

    private fun setupObservers() {
        with(viewModel) {
            notifications.observe(viewLifecycleOwner) {
                notificationsAdapter.submitData(viewLifecycleOwner.lifecycle, it)

                binding.notificationsRecycler.isVisible = true
                binding.errorTxt.isVisible = false
            }
        }
    }

    override fun onClick(announcementId: String, title: String) {
        val action = NotificationsFragmentDirections.actionNotificationsFragmentToDetailsFragment(announcementId, title)
        findNavController().safeNavigate(R.id.notificationsFragment, action)
    }
}