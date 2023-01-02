package com.adam.submissionstoryapp.ui.auth.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.adam.submissionstoryapp.R
import com.adam.submissionstoryapp.databinding.FragmentRegisterBinding
import com.adam.submissionstoryapp.ui.ViewModelFactory
import com.adam.submissionstoryapp.utils.Result

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels { ViewModelFactory(requireContext()) }
    private var email: String? = null
    private var password: String? = null
    private var name: String? = null
    private var loading: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editTextNama.doOnTextChanged{text, _, _, _ ->
            name = text.toString()
        }
        binding.editTextEmail.doOnTextChanged{text, _,_,_ ->
            email = text.toString()
        }
        binding.editTextPassword.doOnTextChanged{text, _,_,_ ->
            password = text.toString()
        }
        setActions()
    }

    private fun setActions() {
        binding.apply {
            btnLogin.setOnClickListener{
                findNavController().popBackStack()
            }

            btnRegister.setOnClickListener {
                if(name != null && email != null&&password != null){
                    viewModel.setRegister(name.toString() ,email.toString(), password.toString()).observe(viewLifecycleOwner){
                        if(it!= null){
                            when(it){
                                is Result.Success ->{
                                    loading = false
                                    showLoading()
                                    Toast.makeText(context, it.data.message, Toast.LENGTH_SHORT).show()
                                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
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
                    }
                } else{
                    Toast.makeText(context,getString(R.string.fields_empty_msg), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading() {
        loading.let{
            binding.registerLayout.visibility = if (it) View.GONE else View.VISIBLE
            binding.loadingLayout.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}