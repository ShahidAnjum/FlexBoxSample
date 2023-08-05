package com.example.flexboxsample

import kotlin.math.ceil


class FlexSizeCalculator {

    private val mSizeForChildAtPosition: MutableList<Size>
    private val mFirstChildPositionForRow: MutableList<Int>
    private val mIncomingSizeList: MutableList<Size>

    init {
        mSizeForChildAtPosition = ArrayList()
        mFirstChildPositionForRow = ArrayList()
        mIncomingSizeList = ArrayList()
    }

    companion object {
        var mContentWidth = 1080
        var mMaxRowHeight = 1000

        fun calculateAspectRatios(flexImages: ArrayList<FlexImage>): ArrayList<Size> {
            val inputSizeList = ArrayList<Size>()
            if (flexImages.isEmpty()) return inputSizeList

            for (index in 0 until flexImages.size) {
                val image = flexImages[index]
                inputSizeList.add(Size(image.width, image.height))
            }
            return FlexSizeCalculator().calculate(inputSizeList)
        }
    }

    fun calculate(incomingSizeList: ArrayList<Size>): ArrayList<Size> {

        if (incomingSizeList.isEmpty()) return ArrayList()

        this.mIncomingSizeList.addAll(incomingSizeList)
        computeChildSizesUpToPosition(mIncomingSizeList.size - 1)
        return mSizeForChildAtPosition as ArrayList<Size>
    }


    private fun computeChildSizesUpToPosition(lastPosition: Int) {
        val firstUncomputedChildPosition = mSizeForChildAtPosition.size
        var currentRowAspectRatio = 0.0
        val itemAspectRatios: MutableList<Double> = ArrayList()
        var currentRowHeight = Int.MAX_VALUE
        var pos = firstUncomputedChildPosition

        // while (pos <= lastPosition || (currentRowHeight > mMaxRowHeight)) {
        while (pos <= lastPosition) {
            val item: Size = mIncomingSizeList[pos]
            val posAspectRatio: Double = item.width.toDouble() / item.height.toDouble()

            // If the size calculator delegate supplies negative aspect ratio,
            // consider it as "span the entire row" view. It will force a line break
            // and add the view to its own line
            var isFullRowView = false
            if (posAspectRatio < 0) {
                isFullRowView = true
            } else {
                currentRowAspectRatio += posAspectRatio
                itemAspectRatios.add(posAspectRatio)
            }
            currentRowHeight = calculateHeight(mContentWidth, currentRowAspectRatio)
            val isRowFull = currentRowHeight <= mMaxRowHeight
            if (isRowFull || isFullRowView) {
                val rowChildCount = itemAspectRatios.size

                // If the current view is the full row view, the current row is forced to wrap so that
                // the full row view can take the entire row for itself, however the first
                // children on that row needs to be added to mFirstChildPositionForRow as well, otherwise
                // the item decoration will not work
                if (isFullRowView) {
                    mFirstChildPositionForRow.add(pos - rowChildCount)
                }
                mFirstChildPositionForRow.add(pos - rowChildCount + 1)
                var availableSpace = mContentWidth
                for (i in 0 until rowChildCount) {
                    // If the previous row was force-wrapped and there was a single photo, the row
                    // size would be computed from that single photo - this could make the row huge
                    // because the aspect ratio of that single photo would be used. So this limits
                    // it to something reasonable
//                    if (isFullRowView && !isRowFull) {
//                        currentRowHeight = (int) Math.ceil(mMaxRowHeight * 0.75);
//                    }
                    var itemWidth = calculateWidth(currentRowHeight, itemAspectRatios[i])
                    itemWidth = Math.min(availableSpace, itemWidth)
                    mSizeForChildAtPosition.add(Size(itemWidth, currentRowHeight))
                    availableSpace -= itemWidth
                }

                itemAspectRatios.clear()
                currentRowAspectRatio = 0.0
            }

            if (pos == lastPosition && mSizeForChildAtPosition.size < mIncomingSizeList.size) {
                mSizeForChildAtPosition.add(Size(mContentWidth, mContentWidth))
            }
            pos++
        }
    }

    private fun calculateWidth(itemHeight: Int, aspectRatio: Double): Int {
        return ceil(itemHeight * aspectRatio).toInt()
    }

    private fun calculateHeight(itemWidth: Int, aspectRatio: Double): Int {
        return ceil(itemWidth / aspectRatio).toInt()
    }
}