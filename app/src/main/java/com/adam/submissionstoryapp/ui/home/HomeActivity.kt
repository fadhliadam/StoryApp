package com.adam.submissionstoryapp.ui.home

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.adam.submissionstoryapp.R
import com.adam.submissionstoryapp.UserPreference
import com.adam.submissionstoryapp.databinding.ActivityHomeBinding
import com.adam.submissionstoryapp.ui.maps.MapsActivity
import com.adam.submissionstoryapp.ui.ViewModelFactory
import com.adam.submissionstoryapp.ui.adapter.LoadingStateAdapter
import com.adam.submissionstoryapp.ui.adapter.StoryAdapter
import com.adam.submissionstoryapp.ui.addstory.AddStoryActivity
import com.adam.submissionstoryapp.ui.auth.AuthenticationActivity

class HomeActivity : AppCompatActivity(){
    private lateinit var binding: ActivityHomeBinding
    private lateinit var storyAdapter: StoryAdapter
    private val storyViewModel: StoryViewModel by viewModels { ViewModelFactory(this) }
    private lateinit var userPreference: UserPreference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.home)
        userPreference = UserPreference(this)
        storyAdapter = StoryAdapter()

        getStoriesData()
        binding.btnAddStory.setOnClickListener{
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_map->{
                startActivity(Intent(this, MapsActivity::class.java))
                true
            }
            R.id.menu_language->{
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.menu_logout->{
                val intent = Intent(this, AuthenticationActivity::class.java)
                startActivity(intent)
                finish()
                userPreference.clearSession()
                Toast.makeText(this, getString(R.string.success_logout), Toast.LENGTH_LONG).show()
                true
            }
            else -> true
        }
    }

    private fun getStoriesData() {
        binding.rvListStory.layoutManager = if(applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            GridLayoutManager(this, 2)
        }else{
            LinearLayoutManager(this)
        }

        storyViewModel.getAllStories().observe(this) {
            binding.rvListStory.adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter{
                    storyAdapter.retry()
                }
            )
            storyViewModel.getAllStories().observe(this){
                storyAdapter.submitData(lifecycle, it)
            }
        }
        binding.rvListStory.adapter = storyAdapter
    }
}