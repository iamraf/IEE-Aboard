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

package gr.ihu.iee.aboard.android.util

import gr.ihu.iee.aboard.android.BuildConfig

const val API_URL = "https://aboard.iee.ihu.gr/api/"

const val CLIENT_ID = ""
const val CLIENT_SECRET = ""

const val LOGIN_URL = "https://login.iee.ihu.gr/"
const val RESPONSE_URL = "https://github.com/iamraf/IEE-Aboard"
const val AUTHORIZATION_URL = "https://login.iee.ihu.gr/authorization?client_id=$CLIENT_ID&redirect_uri=$RESPONSE_URL&response_type=code&scope=announcements,profile,notifications,refresh_token"

const val GRAND_TYPE_AUTHORIZATION = "authorization_code"
const val GRAND_TYPE_REFRESH_TOKEN = "refresh_token"

const val COPY_URL = "https://aboard.iee.ihu.gr/announcements/"

const val PRIVACY_URL = "https://github.com/iamraf/IEE-Aboard/blob/master/PRIVACY-POLICY.md"
const val TOS_URL = "https://github.com/iamraf/IEE-Aboard/blob/master/TERMS-AND-CONDITIONS.md"
const val GITHUB_URL = "https://github.com/iamraf/IEE-Aboard"

const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
const val MARKET_URL = "market://details?id=" + BuildConfig.APPLICATION_ID

const val CONTACT_EMAIL = "iamraf@pm.me"