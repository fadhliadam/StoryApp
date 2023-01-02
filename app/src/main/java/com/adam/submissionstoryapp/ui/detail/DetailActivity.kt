package com.adam.submissionstoryapp.ui.detail

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adam.submissionstoryapp.R
import com.adam.submissionstoryapp.databinding.ActivityDetailBinding
import com.adam.submissionstoryapp.model.StoryModel
import com.bumptech.glide.Glide
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val storyData = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_STORY_DATA, StoryModel::class.java)
        } else {
            intent.getParcelableExtra<StoryModel>(EXTRA_STORY_DATA) as StoryModel
        }
        supportActionBar?.title = getString(R.string.detail_story)

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        sdf.timeZone = TimeZone.getTimeZone("ICT")
        val time = storyData?.createdAt?.let { sdf.parse(it)?.time }
        val prettyTime = PrettyTime(Locale.getDefault())
        val ago = prettyTime.format(time?.let { Date(it) })
        with(binding){
            Glide.with(this@DetailActivity)
                .load(storyData?.photoUrl)
                .into(imgStoryDetail)
            username.text = storyData?.name
            dateCreatedStory.text = ago
            descriptionContent.text = storyData?.description
        }
    }

    companion object {
        const val EXTRA_STORY_DATA = "extra_username"
    }
}