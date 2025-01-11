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

package gr.ihu.iee.aboard.android.util.date

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {

    private const val DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm"

    fun isDateValid(dateString: String): Boolean {
        val formatter = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())

        return try {
            val date = formatter.parse(dateString)

            date?.after(Date()) ?: false
        } catch (e: Exception) {
            false
        }
    }
}