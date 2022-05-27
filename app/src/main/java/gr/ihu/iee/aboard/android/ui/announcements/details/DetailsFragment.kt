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

package gr.ihu.iee.aboard.android.ui.announcements.details

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import gr.ihu.iee.aboard.android.R
import gr.ihu.iee.aboard.android.base.BaseMenuFragment
import gr.ihu.iee.aboard.android.databinding.FragmentDetailsBinding
import gr.ihu.iee.aboard.android.domain.announcements.entity.Announcement
import gr.ihu.iee.aboard.android.domain.announcements.entity.Attachment
import gr.ihu.iee.aboard.android.ui.announcements.adapter.TagsMultiRowAdapter
import gr.ihu.iee.aboard.android.util.API_URL
import gr.ihu.iee.aboard.android.util.COPY_URL
import gr.ihu.iee.aboard.android.util.extentions.getMessage
import gr.ihu.iee.aboard.android.util.manager.PreferencesManager
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : BaseMenuFragment<FragmentDetailsBinding>(), AttachmentsAdapter.AttachmentsAdapterListener {

    override fun initViewBinding(): FragmentDetailsBinding = FragmentDetailsBinding.inflate(layoutInflater)

    private val viewModel: DetailsViewModel by viewModels()
    private val args: DetailsFragmentArgs by navArgs()

    private val attachmentsAdapter: AttachmentsAdapter by lazy { AttachmentsAdapter(this) }

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_details, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.copy -> {
                val clipboard: ClipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("announcement", COPY_URL + args.id)

                clipboard.setPrimaryClip(clip)
                Toast.makeText(activity, getString(R.string.copy_success), Toast.LENGTH_SHORT).show()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupViews() {
        with(binding) {
            tagsRecycler.apply {
                setHasFixedSize(false)
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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bodyTxt.text = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
            } else {
                @Suppress("DEPRECATION")
                bodyTxt.text = Html.fromHtml(text)
            }

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
                val list = mutableListOf<String>()

                announcement.tags.forEach {
                    list.add(it.title)
                }

                tagsRecycler.adapter = TagsMultiRowAdapter(list)
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
        var url = "${API_URL}announcements/${item.announcementId}/attachments/${item.id}?action=download"

        preferencesManager.accessToken?.let {
            url += "&access_token=$it"
        }

        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}