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

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
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
import androidx.navigation.fragment.navArgs
import com.google.android.flexbox.FlexDirection.ROW
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import gr.ihu.iee.aboard.android.R
import gr.ihu.iee.aboard.android.base.BaseFragment
import gr.ihu.iee.aboard.android.databinding.FragmentDetailsBinding
import gr.ihu.iee.aboard.android.domain.announcements.entity.Announcement
import gr.ihu.iee.aboard.android.domain.announcements.entity.Attachment
import gr.ihu.iee.aboard.android.util.COPY_URL
import gr.ihu.iee.aboard.android.util.extentions.getMessage
import gr.ihu.iee.aboard.android.util.manager.PreferencesManager
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentDetailsBinding>(), AttachmentsAdapter.AttachmentsAdapterListener {

    override fun initViewBinding(): FragmentDetailsBinding = FragmentDetailsBinding.inflate(layoutInflater)

    private val viewModel: DetailsViewModel by viewModels()
    private val args: DetailsFragmentArgs by navArgs()

    private val attachmentsAdapter: AttachmentsAdapter by lazy { AttachmentsAdapter(this) }
    private val tagsAdapter: TagsAdapter by lazy { TagsAdapter() }

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_details, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.copy -> {
                        val clipboard: ClipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("announcement", COPY_URL + args.id)

                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(activity, getString(R.string.copy_success), Toast.LENGTH_SHORT).show()

                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        with(binding) {
            tagsRecycler.apply {
                val manager = FlexboxLayoutManager(context)
                manager.flexDirection = ROW

                layoutManager = manager
                adapter = tagsAdapter
            }

            attachmentRecycler.apply {
                setHasFixedSize(false)
                adapter = attachmentsAdapter
            }
        }
    }

    private fun setupObservers() {
        with(viewModel) {
            announcement.observe(viewLifecycleOwner) {
                updateAnnouncement(it)
            }

            isLoading.observe(viewLifecycleOwner) {
                updateProgress(it)
            }

            responseError.observe(viewLifecycleOwner) {
                showError(it.getMessage(requireContext()))
            }

            fetchAnnouncement(args.id)
        }
    }

    private fun updateAnnouncement(announcement: Announcement) {
        with(binding) {
            val text = announcement.body

            bodyTxt.text = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)

            titleTxt.text = announcement.title
            authorTxt.text = announcement.author
            dateTxt.text = announcement.updatedAt

            if (announcement.attachments.isEmpty()) {
                attachmentsLayout.visibility = View.GONE
            } else {
                attachmentsLayout.visibility = View.VISIBLE
                attachmentsAdapter.submitList(announcement.attachments)
            }

            if (announcement.tags.isNotEmpty()) {
                tagsAdapter.submitList(announcement.tags.map { it.title })
            }

            contentLayout.isVisible = true
        }
    }

    private fun updateProgress(flag: Boolean) {
        with(binding) {
            when (flag) {
                true -> {
                    progressBar.isVisible = true
                    contentLayout.isVisible = false
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

    override fun onClick(item: Attachment) {
        var url = item.url

        preferencesManager.accessToken?.let {
            url += "&access_token=$it"
        }

        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}