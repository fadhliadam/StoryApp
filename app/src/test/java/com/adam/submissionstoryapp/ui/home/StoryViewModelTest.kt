package com.adam.submissionstoryapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.adam.submissionstoryapp.DataDummy
import com.adam.submissionstoryapp.MainDispatcherRule
import com.adam.submissionstoryapp.getOrAwaitValue
import com.adam.submissionstoryapp.network.local.repository.StoryRepository
import com.adam.submissionstoryapp.network.responses.GetAllStoriesResponse
import com.adam.submissionstoryapp.network.responses.ListStoryItem
import com.adam.submissionstoryapp.ui.adapter.StoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import com.adam.submissionstoryapp.utils.Result
import org.junit.Before


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock private lateinit var storyRepository: StoryRepository

    private lateinit var storyViewModel: StoryViewModel
    private val dummyStory = DataDummy.generateDummyListStory()
    private val dummyStoryResponse = DataDummy.generateDummyStoriesResponse()

    @Before
    fun setUp() {
        storyViewModel = StoryViewModel(storyRepository)
    }

    @Test
    fun `when Get all Stories Should Not Null`() = runTest {
        val data: PagingData<ListStoryItem> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = data
        `when`(storyRepository.getAllStories()).thenReturn(expectedStory)

        val actualStory: PagingData<ListStoryItem> = storyViewModel.getAllStories().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory, differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0].id, differ.snapshot()[0]?.id)
    }

    @Test
    fun `when Get All Stories with Location Should Not Null and Return Stories Result` () = runTest {
        val expectedResponse = MutableLiveData<Result<GetAllStoriesResponse>>()
        expectedResponse.value = Result.Success(dummyStoryResponse)

        `when`(storyRepository.getAllStoriesWithLocation()).thenReturn(expectedResponse)

        val actualResponse = storyViewModel.getDataLocation().getOrAwaitValue()

        Mockito.verify(storyRepository).getAllStoriesWithLocation()

        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
        assertEquals(
            dummyStory,
            (actualResponse as Result.Success).data.listStory
        )
    }

    @Test
    fun `when Get All Stories With Location Network Error Should Return Error` (){
        val expectedResponse = MutableLiveData<Result<GetAllStoriesResponse>>()
        expectedResponse.value = Result.Error("Error")
        `when`(storyRepository.getAllStoriesWithLocation()).thenReturn(expectedResponse)
        val actualResponse = storyViewModel.getDataLocation().getOrAwaitValue()
        Mockito.verify(storyRepository).getAllStoriesWithLocation()
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<ListStoryItem>>>() {
    companion object {
        fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}