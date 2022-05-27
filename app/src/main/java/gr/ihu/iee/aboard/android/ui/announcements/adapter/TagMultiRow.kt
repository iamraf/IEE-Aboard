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

package gr.ihu.iee.aboard.android.ui.announcements.adapter

import kotlin.math.ceil

class TagMultiRow {

    private var freeSpans = MeasureHelper.SPAN_COUNT
    val tagList = mutableListOf<String>()
    val spanList = mutableListOf<Int>()

    fun addTag(spanRequired: Float, tag: String): Boolean {
        if (spanRequired < freeSpans && tagList.add(tag)) {
            val spanRequiredInt = ceil(spanRequired).toInt()
            spanList.add(spanRequiredInt)
            freeSpans -= spanRequiredInt
            return true
        }

        return false
    }
}