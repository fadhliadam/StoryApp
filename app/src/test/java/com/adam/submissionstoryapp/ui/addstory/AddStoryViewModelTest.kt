package com.adam.submissionstoryapp.ui.addstory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.adam.submissionstoryapp.DataDummy
import com.adam.submissionstoryapp.MainDispatcherRule
import com.adam.submissionstoryapp.getOrAwaitValue
import com.adam.submissionstoryapp.network.local.repository.StoryRepository
import com.adam.submissionstoryapp.network.responses.AddNewStoryResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import com.adam.submissionstoryapp.utils.Result
import org.junit.Before

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock private lateinit var storyRepository: StoryRepository

    private lateinit var addStoryViewModel: AddStoryViewModel
    private val dummyUploadResponse = DataDummy.generateDummyUploadResponse()
    private val file = DataDummy.generateDummyMultipartFile()
    private val desc = DataDummy.generateDummyRequestBody()

    @Before
    fun setUp(){
        addStoryViewModel = AddStoryViewModel(storyRepository)
    }

    @Test
    fun `when Add Story should not error`() {
        val expectedResponse = MutableLiveData<Result<AddNewStoryResponse>>()
        expectedResponse.value = Result.Success(dummyUploadResponse)
        Mockito.`when`(storyRepository.addNewStory(file,desc,0.0, 0.0)).thenReturn(
            expectedResponse
        )

        val actualResponse = addStoryViewModel.addNewStory(file,desc,0.0, 0.0).getOrAwaitValue()

        Mockito.verify(storyRepository).addNewStory(file, desc,0.0, 0.0)
        assertTrue(actualResponse is Result.Success)
    }
}