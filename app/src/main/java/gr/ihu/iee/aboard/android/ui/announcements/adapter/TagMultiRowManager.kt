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

class TagMultiRowManager {

    private val rowList = mutableListOf<TagMultiRow>().apply {
        add(TagMultiRow())
    }

    fun add(spanRequired: Float, tag: String) {
        for (i in 0..rowList.size) {
            val tagRow = rowList[i]

            if (tagRow.addTag(spanRequired, tag)) {
                break
            }

            if (i == rowList.lastIndex) {
                rowList.add(TagMultiRow())
            }
        }
    }

    fun getSortedSpans() = mutableListOf<Int>().apply {
        rowList.forEach {
            addAll(it.spanList)
        }
    }

    fun getSortedTags() = mutableListOf<String>().apply {
        rowList.forEach {
            addAll(it.tagList)
        }
    }
}