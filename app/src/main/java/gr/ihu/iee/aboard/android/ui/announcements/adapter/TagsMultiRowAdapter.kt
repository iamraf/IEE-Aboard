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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import gr.ihu.iee.aboard.android.databinding.ItemTagBinding
import kotlin.properties.Delegates

class TagsMultiRowAdapter(
    private var tagList: MutableList<String>
) : RecyclerView.Adapter<TagsMultiRowAdapter.TagViewHolder>() {

    private val measureHelper = MeasureHelper(this, tagList.size)
    private var recyclerView: RecyclerView? = null
    private var ready = false

    var measuringDone by Delegates.observable(false) { _, _, newVal ->
        if (newVal) {
            update()
        }
    }

    private fun update() {
        recyclerView ?: return
        recyclerView?.apply {
            visibility = View.VISIBLE

            tagList = measureHelper.getItems()

            layoutManager = MultipleSpanGridLayoutManager(context, MeasureHelper.SPAN_COUNT, measureHelper.getSpans())
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        this.recyclerView = recyclerView.apply {
            visibility = View.INVISIBLE

            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    measureHelper.measureBaseCell(recyclerView.width)
                    ready = true

                    notifyDataSetChanged()
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTagBinding.inflate(inflater, parent, false)

        return TagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val tag = tagList[position]
        val shouldMeasure = measureHelper.shouldMeasure()

        holder.bind(tag, shouldMeasure)

        if (shouldMeasure) {
            measureHelper.measure(holder, tag)
        }
    }

    override fun getItemCount() = if (ready) tagList.size else 0

    inner class TagViewHolder(private val binding: ItemTagBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String, shouldMeasure: Boolean) {
            with(binding) {
                root.apply {
                    layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
                }

                titleTxt.apply {
                    text = item
                }

                if (!shouldMeasure) {
                    root.layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
                }
            }
        }
    }
}