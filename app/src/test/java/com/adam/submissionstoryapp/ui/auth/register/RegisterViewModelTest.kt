package com.adam.submissionstoryapp.ui.auth.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.adam.submissionstoryapp.DataDummy
import com.adam.submissionstoryapp.MainDispatcherRule
import com.adam.submissionstoryapp.getOrAwaitValue
import com.adam.submissionstoryapp.network.local.repository.StoryRepository
import com.adam.submissionstoryapp.network.responses.RegisterResponse
import com.adam.submissionstoryapp.ui.auth.AuthModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import com.adam.submissionstoryapp.utils.Result
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock private lateinit var storyRepository: StoryRepository

    private lateinit var registerViewModel: RegisterViewModel
    private val dummyRegisterResponse = DataDummy.generateDummyRegisterResponse()
    private lateinit var registerModel : AuthModel

    @Before
    fun setUp(){
        registerViewModel = RegisterViewModel(storyRepository)
        registerModel = AuthModel(
            name = "name",
            email = "email@email.com",
            password = "password"
        )
    }

    @Test
    fun `when Register should not error and return register result`(){
        val expectedResponse = MutableLiveData<Result<RegisterResponse>>()
        expectedResponse.value = Result.Success(dummyRegisterResponse)
        Mockito.`when`(storyRepository.setRegister(
            registerModel.name,registerModel.email,registerModel.password)
        ).thenReturn(expectedResponse)

        val actualResponse = registerViewModel.setRegister(
            registerModel.name,registerModel.email,registerModel.password).getOrAwaitValue()

        Mockito.verify(storyRepository).setRegister(
            registerModel.name,registerModel.email,registerModel.password)

        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
    }

    @Test
    fun `when Network Error Should Return Error` (){
        val expectedResponse = MutableLiveData<Result<RegisterResponse>>()
        expectedResponse.value = Result.Error("Error")
        Mockito.`when`(storyRepository.setRegister(
            registerModel.name,registerModel.email,registerModel.password
        )).thenReturn(expectedResponse)
        val actualResponse = registerViewModel.setRegister(
            registerModel.name,registerModel.email,registerModel.password
        ).getOrAwaitValue()

        Mockito.verify(storyRepository).setRegister(
            registerModel.name,registerModel.email,registerModel.password
        )

        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
    }
}