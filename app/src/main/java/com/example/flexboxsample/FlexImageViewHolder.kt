/*
 * Copyright 2017 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.flexboxsample

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.flexbox.FlexboxLayoutManager


/**
 * ViewHolder that represents a cat image.
 */
internal class FlexImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val imageView: AppCompatImageView = itemView.findViewById(R.id.ivExplore)
    val parentView: ConstraintLayout = itemView.findViewById(R.id.root_view)

    internal fun bindTo(flexImage: FlexImage) {

        val lp: ViewGroup.LayoutParams = parentView.layoutParams
        if (lp is FlexboxLayoutManager.LayoutParams) {
            lp.height = flexImage.size.height
            lp.width = flexImage.size.width
            lp.flexGrow = flexImage.size.width.toFloat()
        }
        Glide.with(imageView.context)
            .load(flexImage.imageId)
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
            .placeholder(R.drawable.ic_placeholder)
            .transition(DrawableTransitionOptions.withCrossFade(1000))
            //.override(flexImage.size.width, flexImage.size.height)
            .into(imageView)
    }
}
