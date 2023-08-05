package com.example.flexboxsample

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ListViewModel(application: Application) : AndroidViewModel(application) {

    private val staticFlexImages = ArrayList<FlexImage>()
    var isLoading: Boolean = false

    private val _flexImages = MutableLiveData<ArrayList<FlexImage>>(ArrayList())
    val flexImages: LiveData<ArrayList<FlexImage>>
        get() = _flexImages


    init {
        initStaticList()
        loadMore()
    }

    private fun initStaticList() {
        staticFlexImages.add(
            FlexImage(
                R.drawable.wide_image,
                1200,
                675
            )
        )
        staticFlexImages.add(
            FlexImage(
                R.drawable.tall_image,
                675,
                1200
            )
        )
        staticFlexImages.add(
            FlexImage(
                R.drawable.landscape_image,
                720,
                540
            )
        )
        staticFlexImages.add(
            FlexImage(
                R.drawable.portrait_image,
                540,
                720
            )
        )
        staticFlexImages.add(
            FlexImage(
                R.drawable.square_image,
                512,
                512
            )
        )
    }

    fun loadMore() {
        isLoading = true

        viewModelScope.launch {
            delay(1000)

            val images = ArrayList<FlexImage>()
            for (index in 0 until 60) {

                images.add(staticFlexImages.random().copy())
            }

            //-- calculate flex size
            val sizes = FlexSizeCalculator.calculateAspectRatios(images)
            for (index in 0 until 60) {
                images[index].size = sizes[index]
            }

            val existing = flexImages.value!!
            existing.addAll(images)
            _flexImages.postValue(existing)
        }
    }

}
