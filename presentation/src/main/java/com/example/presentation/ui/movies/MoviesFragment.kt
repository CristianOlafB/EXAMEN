package com.example.presentation.ui.movies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.presentation.databinding.FragmentMoviesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private val characterListViewModel by viewModels<MoviesViewModel>()

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private val movieAdapter by lazy {
        MoviesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCharacterRecyclerView()
        setUpSwipeToRefresh()
        collectFromViewModel()
    }

    private fun setUpSwipeToRefresh() {
        binding.swipeRefresh.apply {
            setOnRefreshListener {
                binding.swipeRefresh.isRefreshing = false
                binding.recyclerView.scrollToPosition(0)
            }
        }
    }

    private fun setupCharacterRecyclerView() {
        binding.apply {
            recyclerView.apply {
                layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                adapter = movieAdapter
            }
        }
    }

    private fun collectFromViewModel() {
        lifecycleScope.launch {
            characterListViewModel.charactersFlow
                .collectLatest {
                    movieAdapter.submitData(it)
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}