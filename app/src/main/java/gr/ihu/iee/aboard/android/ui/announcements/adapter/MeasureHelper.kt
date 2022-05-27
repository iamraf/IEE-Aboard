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

import android.view.ViewGroup
import android.view.ViewTreeObserver

class MeasureHelper(
    private val adapter: TagsMultiRowAdapter,
    private val count: Int
) {

    companion object {
        const val SPAN_COUNT = 52
    }

    private var measuredCount = 0
    private val rowManager = TagMultiRowManager()
    private var baseCell: Float = 0f

    fun measureBaseCell(width: Int) {
        baseCell = (width / SPAN_COUNT).toFloat()
    }

    fun shouldMeasure() = measuredCount != count
    fun getItems() = rowManager.getSortedTags()
    fun getSpans() = rowManager.getSortedSpans()

    private fun cellMeasured() {
        if (!adapter.measuringDone && !shouldMeasure()) {
            adapter.measuringDone = true
        }
    }

    fun measure(holder: TagsMultiRowAdapter.TagViewHolder, tag: String) {
        val itemView = holder.itemView.apply {
            layoutParams.height = 1
        }

        val globalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                itemView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val marginTotal = (itemView.layoutParams as ViewGroup.MarginLayoutParams).marginStart * 2
                val span = (itemView.width + marginTotal) / baseCell

                measuredCount++

                rowManager.add(span, tag)

                cellMeasured()
            }
        }

        itemView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }
}