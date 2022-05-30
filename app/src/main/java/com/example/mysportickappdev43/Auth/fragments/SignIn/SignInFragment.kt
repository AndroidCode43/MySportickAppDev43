package com.example.mysportickappdev43.Auth.fragments.SignIn

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.mysportickappdev43.Auth.utlis.AuthListener
import com.example.mysportickappdev43.Main.MainActivity
import com.example.mysportickappdev43.R
import com.example.mysportickappdev43.databinding.FragmentSignInBinding
import com.example.mysportickappdev43.utils.showToastFragment

class SignInFragment : Fragment(R.layout.fragment_sign_in) , AuthListener{

    private lateinit var mBinding : FragmentSignInBinding
    private val mViewModel : SignInViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentSignInBinding.bind(view)
        init()
    }

    private fun init(){
        mViewModel.authListener = this
        initBtn()
    }

    private fun initBtn(){
        mBinding.apply {
            btnCreateAccountNavigate.setOnClickListener {
                //init navigation
                Navigation
                    .findNavController(requireView())
                    .navigate(R.id.action_signInFragment_to_createAccountFragment)
            }
        }
    }

    override fun onSuccessAccount(msg: String) {
        //showToastFragment(msg)
        //startActivity(Intent(requireActivity(), MainActivity::class.java))
    }

    override fun onErrorAccount(msg: String) {
        showToastFragment(msg)
    }
}