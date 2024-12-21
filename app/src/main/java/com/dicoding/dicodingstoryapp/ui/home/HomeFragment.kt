package com.dicoding.dicodingstoryapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingstoryapp.R
import com.dicoding.dicodingstoryapp.ViewModelFactory
import com.dicoding.dicodingstoryapp.databinding.FragmentHomeBinding
import com.dicoding.dicodingstoryapp.ui.settings.SettingsActivity
import com.dicoding.dicodingstoryapp.ui.story.AddStoryActivity
import com.dicoding.dicodingstoryapp.ui.story.DetailStoryActivity
import com.dicoding.dicodingstoryapp.ui.story.StoryAdapter

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private val adapter = StoryAdapter(::navigateToDetail)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHomeBinding.bind(view)
        _binding = binding

        _binding?.rvStory?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = this@HomeFragment.adapter
        }

        _binding?.btnAddStory?.setOnClickListener {
            navigateToAddStory()
        }

        _binding?.setting?.setOnClickListener {
            navigateToSetting()
        }

        with(viewModel) {
            listStory.observe(viewLifecycleOwner) { stories ->
                adapter.submitList(stories)
                binding.rvStory.visibility = View.VISIBLE
            }

            isLoading.observe(viewLifecycleOwner) { isLoading ->
                showLoading(isLoading)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getStoriesWithToken()
    }

    private fun showLoading(isLoading: Boolean) {
        _binding?.progressBarStory?.visibility = if (isLoading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun navigateToSetting(){
        val intent = Intent(requireContext(), SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToAddStory() {
        val intent = Intent(requireContext(), AddStoryActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToDetail(id: String){
        DetailStoryActivity.start(requireContext(), id)
    }
}