package com.dicoding.dicodingstoryapp.ui.story

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.dicodingstoryapp.ViewModelFactory
import com.dicoding.dicodingstoryapp.data.response.Story
import com.dicoding.dicodingstoryapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    private val viewModel by viewModels<DetailStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        val storyId = intent.getStringExtra(STORY_ID)

        if(storyId != null){
            viewModel.getStoryById(storyId)
        } else{
            Toast.makeText(this, "Invalid story ID", Toast.LENGTH_SHORT).show()
        }

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.detailStory.observe(this) { story ->
            showDetailStory(story)
        }
    }

    private fun showDetailStory(story: Story?) {
        with(binding){
            Glide.with(root)
                .load(story?.photoUrl)
                .into(storyImage)
            username.text = story?.name
            storyDescription.text = story?.description
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object{
        const val STORY_ID = "story_id"

        fun start(context: Context, storyId: String){
            val intent = Intent(context, DetailStoryActivity::class.java)
            intent.putExtra(STORY_ID, storyId)
            context.startActivity(intent)
        }
    }
}