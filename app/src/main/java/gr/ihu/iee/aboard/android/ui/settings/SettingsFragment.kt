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

package gr.ihu.iee.aboard.android.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import gr.ihu.iee.aboard.android.BuildConfig
import gr.ihu.iee.aboard.android.R
import gr.ihu.iee.aboard.android.base.BaseFragment
import gr.ihu.iee.aboard.android.databinding.FragmentSettingsBinding
import gr.ihu.iee.aboard.android.ui.main.MainActivity
import gr.ihu.iee.aboard.android.util.*
import gr.ihu.iee.aboard.android.util.extentions.getMessage
import gr.ihu.iee.aboard.android.util.extentions.safeNavigate
import gr.ihu.iee.aboard.android.util.helper.ThemeHelper

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    override fun initViewBinding(): FragmentSettingsBinding = FragmentSettingsBinding.inflate(layoutInflater)

    private val viewModel: SettingsViewModel by viewModels()

    private lateinit var logoutDialog: AlertDialog
    private lateinit var themeDialog: AlertDialog
    private lateinit var bugDialog: AlertDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createDialogs()
        setupViews()
        setupObservers()
    }

    private fun createDialogs() {
        logoutDialog = MaterialAlertDialogBuilder(requireContext(), R.style.MyAlertDialogTheme)
            .setTitle(R.string.logout)
            .setMessage(R.string.are_you_sure_you_want_to_logout)
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.logout()
            }
            .setNegativeButton(R.string.no, null)
            .create()

        themeDialog = MaterialAlertDialogBuilder(requireContext(), R.style.MyAlertDialogTheme)
            .setTitle(R.string.theme)
            .setSingleChoiceItems(
                R.array.themeList,
                resources.getStringArray(R.array.themeEntry).indexOf(viewModel.getTheme())
            ) { dialog, which ->
                val newTheme = resources.getStringArray(R.array.themeEntry)[which]
                viewModel.setTheme(newTheme)
                ThemeHelper.applyTheme(newTheme)

                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel, null)
            .create()

        bugDialog = MaterialAlertDialogBuilder(requireContext(), R.style.MyAlertDialogTheme)
            .setTitle(R.string.report)
            .setMessage(R.string.report_problem_text)
            .setPositiveButton(R.string.ok) { _, _ ->
                val title = getString(R.string.app_name) + " " + BuildConfig.VERSION_NAME + " - " +
                        getString(R.string.report) + " (" + Build.MODEL + ", " + Build.VERSION.SDK_INT + ")"
                val body = getString(R.string.report_your_problem_here)
                val data = "mailto:" + CONTACT_EMAIL + "?subject=" + Uri.encode(title) + "&body=" + Uri.encode(body)

                val emailIntent = Intent(Intent.ACTION_SENDTO)
                emailIntent.data = Uri.parse(data)
                startActivity(emailIntent)
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }

    private fun setupViews() {
        with(binding) {
            logout.setOnClickListener {
                logoutDialog.show()
            }

            tags.setOnClickListener {
                val action = SettingsFragmentDirections.actionProfileFragmentToSubscribeFragment()
                findNavController().safeNavigate(R.id.settingsFragment, action)
            }

            theme.setOnClickListener {
                themeDialog.show()
            }

            notifications.setOnClickListener {
                viewModel.setNotifications()
            }

            rate.setOnClickListener {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URL)))
                } catch (e: Exception) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_URL)))
                }
            }

            bug.setOnClickListener {
                bugDialog.show()
            }

            privacy.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_URL)))
            }

            tos.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(TOS_URL)))
            }

            sourcecode.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_URL)))
            }

            versionTxt.text = resources.getString(R.string.version, BuildConfig.VERSION_NAME)
        }
    }

    private fun setupObservers() {
        with(viewModel) {
            logoutSuccessful.observe(viewLifecycleOwner) {
                val intent = Intent(activity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }

            responseError.observe(viewLifecycleOwner) {
                Toast.makeText(context, it.getMessage(requireContext()), Toast.LENGTH_SHORT).show()
            }

            isLoading.observe(viewLifecycleOwner) {
                binding.progressBar.isVisible = it
            }

            areNotificationsEnabled.observe(viewLifecycleOwner) {
                binding.checkBox.isChecked = it
            }
        }
    }
}