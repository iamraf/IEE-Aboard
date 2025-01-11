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

package gr.ihu.iee.aboard.android.util.extentions

import android.content.Context
import gr.ihu.iee.aboard.android.R

const val UNKNOWN_ERROR_CODE = -1
const val NO_INTERNET_CONNECTION_CODE = 0
const val TIME_OUT_CODE = 1
const val REQUEST_ERROR_CODE = 2
const val REQUEST_UNAUTHENTICATED_CODE = 3
const val REQUEST_NO_ANNOUNCEMENTS_CODE = 4

open class ResponseException(val code: Int, val error: String) : Exception()

fun ResponseException.getMessage(context: Context): String =
    when (code) {
        NO_INTERNET_CONNECTION_CODE -> context.getString(R.string.no_internet_connection)
        TIME_OUT_CODE -> context.getString(R.string.time_out)
        REQUEST_ERROR_CODE, REQUEST_UNAUTHENTICATED_CODE -> error
        REQUEST_NO_ANNOUNCEMENTS_CODE -> context.getString(R.string.no_announcements_found)
        else -> context.getString(R.string.unknown_error)
    }