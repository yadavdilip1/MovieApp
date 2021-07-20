package com.assesment.movies.ui.movies

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.assesment.movies.databinding.FragmentMovieBinding
import com.assesment.movies.utils.NetworkHelper
import com.assesment.movies.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.error_screen.view.*
import kotlinx.android.synthetic.main.no_internet_layout.view.*
import javax.inject.Inject

@AndroidEntryPoint
class MovieFragment : Fragment() {

    @Inject
    lateinit var networkHelper: NetworkHelper

    private lateinit var binding: FragmentMovieBinding
    private val viewModel: MovieViewModel by viewModels()
    private val movieAdapter = MoviePagingAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root

    }


    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setRecyclerView()


        viewModel.list.observe(viewLifecycleOwner) {
            movieAdapter.submitData(lifecycle, it)
        }

        movieAdapter.addLoadStateListener { loadState ->

            if (loadState.refresh is LoadState.Loading) {

                binding.progressBar.visibility = View.VISIBLE

                binding.movieRecycler.visibility = View.GONE
                binding.noItemText.visibility = View.GONE
                binding.noInternet.isVisible = false
                binding.errorLyt.isVisible = false


            } else {

                binding.progressBar.visibility = View.GONE

                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    movieAdapter.itemCount < 1
                ) {
                    binding.movieRecycler.visibility = View.GONE
                    binding.noItemText.visibility = View.VISIBLE

                } else {

                    binding.noItemText.visibility = View.GONE
                    binding.movieRecycler.visibility = View.VISIBLE
                }

                val errorState = when (loadState.refresh) {
                    is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }

                errorState?.let {

                    if (movieAdapter.itemCount < 1) {
                        binding.noInternet.isVisible = !networkHelper.isNetworkConnected()
                        binding.errorLyt.isVisible = networkHelper.isNetworkConnected()

                    } else {
                        if (networkHelper.isNetworkConnected()) {
                            snackbar("Something went wrong")
                        } else {
                            snackbar("No Internet Connection")
                        }
                    }
                }
            }
        }


        movieAdapter.onMovieClick {
            val action = MovieFragmentDirections.actionMovieFragmentToDetailsFragment(it)
            findNavController().navigate(action)
        }

    }

    private fun setRecyclerView() {

        binding.noInternet.retry.setOnClickListener { movieAdapter.retry() }
        binding.errorLyt.err_retry.setOnClickListener { movieAdapter.retry() }


        binding.movieRecycler.apply {
            adapter = movieAdapter
            layoutManager =
                if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    GridLayoutManager(requireContext(), 3)
                } else {
                    GridLayoutManager(requireContext(), 5)
                }
            movieAdapter.withLoadStateHeaderAndFooter(
                header = PagingLoadStateAdapter { movieAdapter.retry() },
                footer = PagingLoadStateAdapter { movieAdapter.retry() }
            )

        }

    }


}