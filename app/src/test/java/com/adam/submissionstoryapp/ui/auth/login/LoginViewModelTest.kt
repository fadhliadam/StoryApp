package com.adam.submissionstoryapp.ui.auth.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.adam.submissionstoryapp.DataDummy
import com.adam.submissionstoryapp.MainDispatcherRule
import com.adam.submissionstoryapp.getOrAwaitValue
import com.adam.submissionstoryapp.network.local.repository.StoryRepository
import com.adam.submissionstoryapp.network.responses.LoginResponse
import com.adam.submissionstoryapp.ui.auth.AuthModel
import com.adam.submissionstoryapp.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock private lateinit var storyRepository: StoryRepository

    private lateinit var loginViewModel: LoginViewModel
    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private lateinit var loginModel : AuthModel

    @Before
    fun setUp(){
        loginViewModel = LoginViewModel(storyRepository)
        loginModel = AuthModel(
            name = "name",
            email = "email",
            password = "password"
        )
    }

    @Test
    fun `when Login should not error and return login result`(){
        val expectedResponse = MutableLiveData<Result<LoginResponse>>()
        expectedResponse.value = Result.Success(dummyLoginResponse)
        Mockito.`when`(storyRepository.setLogin(
            loginModel.email,loginModel.password)).thenReturn(expectedResponse)

        val actualResponse = loginViewModel.setLogin(
            loginModel.email,loginModel.password).getOrAwaitValue()

        Mockito.verify(storyRepository).setLogin(loginModel.email,loginModel.password)

        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
        assertEquals(
            (expectedResponse.value as Result.Success).data.loginResult,
            (actualResponse as Result.Success).data.loginResult
        )
    }

    @Test
    fun `when Network Error Should Return Error` (){
        val expectedResponse = MutableLiveData<Result<LoginResponse>>()
        expectedResponse.value = Result.Error("Error")
        Mockito.`when`(storyRepository.setLogin(loginModel.email,loginModel.password)).thenReturn(expectedResponse)
        val actualResponse = loginViewModel.setLogin(loginModel.email,loginModel.password).getOrAwaitValue()

        Mockito.verify(storyRepository).setLogin(loginModel.email,loginModel.password)

        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
    }
}