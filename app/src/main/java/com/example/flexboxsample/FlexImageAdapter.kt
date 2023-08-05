package com.example.flexboxsample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

data class Size(var width: Int, var height: Int)

data class FlexImage(
    val imageId: Int,
    val width: Int,
    val height: Int,
    var size: Size = Size(512, 512)
)

internal class FlexImageAdapter(private val flexImages: ArrayList<FlexImage>) :
    RecyclerView.Adapter<FlexImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlexImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_cat, parent, false)
        return FlexImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlexImageViewHolder, position: Int) {
        holder.bindTo(flexImages[position])
    }

    override fun getItemCount() = flexImages.size

    fun addData(list: List<FlexImage>) {
        flexImages.clear()
        flexImages.addAll(list)
        notifyDataSetChanged()
    }
}
