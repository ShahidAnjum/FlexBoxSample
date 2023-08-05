package com.example.flexboxsample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.flexboxsample.databinding.FragmentListBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager


class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private lateinit var flexImageAdapter: FlexImageAdapter
    private lateinit var binding: FragmentListBinding
    private lateinit var flexboxLayoutManager: FlexboxLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.flexImages.observe(viewLifecycleOwner) {
            flexImageAdapter.addData(it)
            binding.swipeRefreshLayout.isRefreshing = false
            viewModel.isLoading = false
        }
    }

    private fun setupUI() {
        flexImageAdapter = FlexImageAdapter(ArrayList())
        flexboxLayoutManager = FlexboxLayoutManager(requireContext()).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
        }

        binding.rvImages.apply {
            layoutManager = flexboxLayoutManager
            adapter = flexImageAdapter
        }


        binding.rvImages.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val flexboxLayoutManager =
                    recyclerView.layoutManager as FlexboxLayoutManager
                val visibleItemCount: Int = flexboxLayoutManager.childCount
                val totalItemCount: Int = flexboxLayoutManager.itemCount
                val firstVisibleItemPosition: Int =
                    flexboxLayoutManager.findFirstVisibleItemPosition()
                if (!viewModel.isLoading
                    && (visibleItemCount + firstVisibleItemPosition >= totalItemCount)
                    && (firstVisibleItemPosition >= 0)
                ) {
                    binding.swipeRefreshLayout.isRefreshing = true
                    viewModel.loadMore()
                }
            }
        })
    }
}