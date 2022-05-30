package com.example.mysportickappdev43.Auth.fragments.CreateAccount

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.mysportickappdev43.Auth.utlis.AuthListener
import com.example.mysportickappdev43.R
import com.example.mysportickappdev43.databinding.FragmentCreateAccountBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateAccountFragment : Fragment(R.layout.fragment_create_account) , AuthListener{

    private lateinit var mBinding : FragmentCreateAccountBinding
    private val mViewModel : CreateAccountViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentCreateAccountBinding.bind(view)
        init()
    }

    private fun init(){
        mViewModel.authListener = this
        initBtn()
    }

    private fun initBtn(){
        mBinding.apply {

            btnSignIn.setOnClickListener {
                Navigation
                    .findNavController(requireView())
                    .navigate(R.id.action_createAccountFragment_to_signInFragment)
            }

            btnCreateAccount.setOnClickListener{
                mViewModel.creteNewAccount(
                    email = this.inputEmail.text.toString(),
                    pass = this.inputPassword.text.toString(),
                    confirmPass = this.inputConfirmPassword.text.toString()
                )
            }
        }
    }

    override fun onSuccessAccount(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onErrorAccount(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}