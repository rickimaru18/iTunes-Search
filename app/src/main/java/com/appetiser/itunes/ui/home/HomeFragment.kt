package com.appetiser.itunes.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.appetiser.itunes.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var _viewModel: HomeViewModel
    private lateinit var _listAdapter: HomeListAdapter

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setupViews()
    }

    /**
     * Setup ViewModels for this fragment.
     */
    private fun setupViewModel() {
        _viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _viewModel.apply {
            config?.observe(viewLifecycleOwner, Observer {
                if (it == null) {
                    return@Observer
                }

                // Set last searched term
                binding.searchTerm.setQuery(it.term, false)

                // Set last selected media type
                var selectedItem = binding.spinnerMediaType.selectedItem as String
                selectedItem = selectedItem.decapitalize().replace(" ", "")
                if (it.media != selectedItem) {
                    for (i in 0 until binding.spinnerMediaType.count) {
                        var spinnerItem = binding.spinnerMediaType.getItemAtPosition(i) as String
                        spinnerItem = spinnerItem.decapitalize().replace(" ", "")

                        if (spinnerItem == it.media) {
                            binding.spinnerMediaType.setSelection(i)
                            break
                        }
                    }
                }

                // Set last updated text
                binding.txtLastVisited.text = "Last updated: ${it.lastUpdated}"
            })

            isFetchingUpdatedTracks.observe(viewLifecycleOwner, Observer {
                binding.swipeRefresh.isRefreshing = it
            })

            tracks.observe(viewLifecycleOwner, Observer {
                _listAdapter.submitList(it)
            })
        }
    }

    /**
     * Setup views for this fragment.
     */
    private fun setupViews() {
        binding.searchTerm.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                _viewModel.searchTracks(query!!, binding.spinnerMediaType.selectedItem as String)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        binding.swipeRefresh.setOnRefreshListener {
            if (binding.searchTerm.query.isNotBlank()) {
                _viewModel.searchTracks(
                    binding.searchTerm.query.toString(),
                    binding.spinnerMediaType.selectedItem as String
                )
            } else {
                _viewModel.searchTracks(media = binding.spinnerMediaType.selectedItem as String)
            }
        }

        _listAdapter = HomeListAdapter() {
            _viewModel.toggleSelectedTrack(it)
        }
        binding.listTracks.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = _listAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
