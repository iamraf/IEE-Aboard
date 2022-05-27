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

package gr.ihu.iee.aboard.android.util.manager

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext context: Context
) {

    companion object {
        private const val PREF_ACCESS_TOKEN = "pref_access_token"
        private const val PREF_REFRESH_TOKEN = "pref_refresh_token"

        private const val PREF_THEME = "pref_theme"
        private const val PREF_NOTIFICATIONS = "pref_notifications"
    }

    private val preferences = context.getSharedPreferences("aboard", Context.MODE_PRIVATE)

    var accessToken: String?
        get() = preferences.getString(PREF_ACCESS_TOKEN, null)
        set(value) {
            preferences.edit().putString(PREF_ACCESS_TOKEN, value).apply()
        }

    var refreshToken: String?
        get() = preferences.getString(PREF_REFRESH_TOKEN, null)
        set(value) {
            preferences.edit().putString(PREF_REFRESH_TOKEN, value).apply()
        }

    var theme: String
        get() = preferences.getString(PREF_THEME, null) ?: "default"
        set(value) {
            preferences.edit().putString(PREF_THEME, value).apply()
        }

    var notifications: Boolean
        get() = preferences.getBoolean(PREF_NOTIFICATIONS, false)
        set(value) {
            preferences.edit().putBoolean(PREF_NOTIFICATIONS, value).apply()
        }
}