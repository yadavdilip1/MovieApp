package com.assesment.movies.ui.moviesdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.assesment.movies.databinding.FragmentDetailsBinding
import com.assesment.movies.ui.movies.MovieViewModel
import com.assesment.movies.utils.NetworkHelper
import com.assesment.movies.utils.Status
import com.assesment.movies.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    @Inject
    lateinit var networkHelper: NetworkHelper
    private lateinit var binding: FragmentDetailsBinding
    private val args: DetailsFragmentArgs by navArgs()
    private val viewModel: MovieViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        binding.backPress.setOnClickListener {
            findNavController().popBackStack()
        }


        viewModel.getMovieDetails(args.imdbId!!)

        viewModel.movieDetails.observe(viewLifecycleOwner) {
            when (it.status) {

                Status.LOADING -> {

                    binding.detailsProgress.visibility = View.VISIBLE

                }
                Status.ERROR -> {
                    binding.detailsProgress.visibility = View.GONE

                    if (networkHelper.isNetworkConnected()) {
                        snackbar("Something went wrong")
                    } else {
                        snackbar("No Internet Connection")
                    }

                }
                Status.SUCCESS -> {

                    binding.detailsProgress.visibility = View.GONE
                    binding.movieDetails = it.data

                }

            }
        }


    }

}