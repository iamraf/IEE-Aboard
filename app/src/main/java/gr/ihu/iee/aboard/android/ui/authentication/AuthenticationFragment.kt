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

package gr.ihu.iee.aboard.android.ui.authentication

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import gr.ihu.iee.aboard.android.R
import gr.ihu.iee.aboard.android.base.BaseFragment
import gr.ihu.iee.aboard.android.databinding.FragmentAuthenticationBinding
import gr.ihu.iee.aboard.android.ui.main.MainActivity
import gr.ihu.iee.aboard.android.util.AUTHORIZATION_URL
import gr.ihu.iee.aboard.android.util.LOGIN_URL
import gr.ihu.iee.aboard.android.util.RESPONSE_URL
import gr.ihu.iee.aboard.android.util.extentions.getMessage


@AndroidEntryPoint
class AuthenticationFragment : BaseFragment<FragmentAuthenticationBinding>() {

    override fun initViewBinding(): FragmentAuthenticationBinding = FragmentAuthenticationBinding.inflate(layoutInflater)

    private val viewModel: AuthenticationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupObservers()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupViews() {
        with(binding) {
            webView.settings.javaScriptEnabled = true
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    val url = request?.url.toString()

                    return if (url.contains(LOGIN_URL)) {
                        webView.loadUrl(url)
                        true
                    } else {
                        if (url.contains(RESPONSE_URL)) {
                            if (url.contains("error")) {
                                Toast.makeText(context, getString(R.string.unknown_error), Toast.LENGTH_SHORT).show()
                                findNavController().popBackStack()
                            } else {
                                val uri: Uri = Uri.parse(url)
                                val code: String? = uri.getQueryParameter("code")
                                viewModel.authenticate(code ?: "")
                            }
                            false
                        } else {
                            webView.loadUrl(AUTHORIZATION_URL)
                            true
                        }
                    }
                }
            }

            webView.loadUrl(AUTHORIZATION_URL)
        }
    }

    private fun setupObservers() {
        with(viewModel) {
            success.observe(viewLifecycleOwner) {
                val intent = Intent(activity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }

            isLoading.observe(viewLifecycleOwner) {
                binding.progressBar.isVisible = it
            }

            responseError.observe(viewLifecycleOwner) {
                Toast.makeText(context, it.getMessage(requireContext()), Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }
}