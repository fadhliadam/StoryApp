package com.adam.submissionstoryapp.ui.addstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.adam.submissionstoryapp.R
import com.adam.submissionstoryapp.databinding.ActivityAddStoryBinding
import com.adam.submissionstoryapp.ui.ViewModelFactory
import com.adam.submissionstoryapp.ui.home.HomeActivity
import com.adam.submissionstoryapp.utils.createCustomTempFile
import com.adam.submissionstoryapp.utils.uriToFile
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import com.adam.submissionstoryapp.utils.Result
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class AddStoryActivity : AppCompatActivity() {
    private val addStoryViewModel: AddStoryViewModel by viewModels { ViewModelFactory(this) }
    private lateinit var addStoryBinding: ActivityAddStoryBinding
    private var getFile: File? = null
    private lateinit var currentPhotoPath: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat: Double = 0.0
    private var lon: Double = 0.0

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result = BitmapFactory.decodeFile(getFile?.path)

            addStoryBinding.imgAddStory.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            uriToFile(selectedImg, this).also { getFile = it }
            addStoryBinding.imgAddStory.setImageURI(selectedImg)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addStoryBinding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(addStoryBinding.root)
        supportActionBar?.title = getString(R.string.add_story)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getMyLocation()

        addStoryBinding.btnGaleri.setOnClickListener { startGallery() }
        addStoryBinding.btnCamera.setOnClickListener{ startTakePhoto() }
        addStoryBinding.btnUpload.isEnabled = false

        buttonUploadIsEnable()
        addStoryBinding.btnUpload.setOnClickListener {
            uploadImage()
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        createCustomTempFile(this@AddStoryActivity.application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.adam.submissionstoryapp.mycamera",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun buttonUploadIsEnable(){
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){
                val descEditTextIsNotEmpty = addStoryBinding.editTextDescription.text.isNotEmpty()
                addStoryBinding.btnUpload.isEnabled = descEditTextIsNotEmpty && getFile!=null
                addStoryBinding.editTextDescription.doOnTextChanged { text, _, _, _ ->
                    addStoryBinding.btnUpload.isEnabled = text.toString().isNotEmpty() && getFile!=null
                }
            }
        }
    }

    private fun uploadImage() {
        val editTextDescription = addStoryBinding.editTextDescription
        val file = getFile as File

        val description = editTextDescription.text.toString().toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )

        addStoryViewModel.addNewStory(imageMultipart,description,lat, lon).observe(this){
            if(it!=null){
                when(it){
                    is Result.Success -> {
                        addStoryBinding.btnUpload.isEnabled = true
                        Toast.makeText(this@AddStoryActivity, it.data.message, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@AddStoryActivity, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                    is Result.Error -> {
                        addStoryBinding.btnUpload.isEnabled = true
                        Toast.makeText(this@AddStoryActivity, it.error, Toast.LENGTH_SHORT).show()
                    }
                    Result.Loading -> addStoryBinding.btnUpload.isEnabled = false
                }
            }
        }
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lat = location.latitude
                    lon = location.longitude
                } else {
                    Toast.makeText(
                        this@AddStoryActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){
            isGranted: Boolean -> if (isGranted) getMyLocation()
    }
}