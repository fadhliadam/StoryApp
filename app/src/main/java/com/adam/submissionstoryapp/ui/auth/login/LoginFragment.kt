package com.adam.submissionstoryapp.ui.auth.login

import android.content.Intent
import com.adam.submissionstoryapp.utils.Result
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.adam.submissionstoryapp.R
import com.adam.submissionstoryapp.UserPreference
import com.adam.submissionstoryapp.databinding.FragmentLoginBinding
import com.adam.submissionstoryapp.model.UserModel
import com.adam.submissionstoryapp.ui.ViewModelFactory
import com.adam.submissionstoryapp.ui.home.HomeActivity

class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by viewModels { ViewModelFactory(requireContext()) }
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var email: String? = null
    private var password: String? = null
    private lateinit var userModel: UserModel
    private lateinit var userPreference: UserPreference
    private var loading: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreference = UserPreference(requireContext())
        userModel = UserModel()
        binding.editTextEmail.doOnTextChanged{ text, _, _, _ ->
            email = text.toString()
        }
        binding.editTextPassword.doOnTextChanged{ text, _, _, _ ->
            password = text.toString()
        }
        setActions()
    }

    private fun setActions() {
        binding.apply {
            btnRegister.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_registerFragment2)
            )

            btnLogin.setOnClickListener {
                if(email != null&&password != null){
                    viewModel.setLogin(email.toString(), password.toString()).observe(viewLifecycleOwner){
                        when(it){
                            is Result.Success ->{
                                userModel.apply {
                                    name = it.data.loginResult?.name
                                    token = it.data.loginResult?.token
                                    userId = it.data.loginResult?.userId
                                }
                                userPreference.setUser(userModel)
                                val intent = Intent(requireContext(), HomeActivity::class.java)
                                Toast.makeText(context, it.data.message, Toast.LENGTH_SHORT).show()
                                loading = false
                                showLoading()
                                activity?.startActivity(intent)
                                requireActivity().finish()
                            }
                            is Result.Error -> {
                                loading = false
                                showLoading()
                                Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                            }
                            Result.Loading -> {
                                loading = true
                                showLoading()
                            }
                        }
                    }
                } else{
                    Toast.makeText(context,getString(R.string.empty_login_form),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading() {
        loading.let{
            binding.loginLayout.visibility = if (it) View.GONE else View.VISIBLE
            binding.loadingLayout.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}