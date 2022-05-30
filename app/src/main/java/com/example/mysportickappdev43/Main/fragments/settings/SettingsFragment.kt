package com.example.mysportickappdev43.Main.fragments.settings

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.example.mysportickappdev43.R
import com.example.mysportickappdev43.databinding.FragmentSettingsBinding
import com.example.mysportickappdev43.room.entity.CurrentUserEntity
import com.example.mysportickappdev43.utils.startNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val viewModel : SettingsViewModel by viewModels()
    private var bind : FragmentSettingsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentSettingsBinding.bind(view)
        lifecycle.addObserver(viewModel)

        init()
    }

    private fun init(){
        initUi()
        dataObservers()
    }

    private fun initUi(){
        clickBtnSaveProfile()
        clickBirthdayField()
        btnExit()
    }

    private fun clickBtnSaveProfile(){
        bind!!.apply {
            btnSaveUserData.setOnClickListener {
                viewModel.updateUserProfile(
                    nick = this.inpUserName.text.toString(),
                    height = this.inpUserHeight.text.toString(),
                    weight = this.inpUserWeight.text.toString(),
                    birthday = this.inpUserAge.text.toString()
                )
            }
        }
    }

    private fun btnExit(){
        bind!!.btnExitSett.setOnClickListener {
            startNavigation(R.id.action_settingsFragment_to_homeFragment)
        }
    }

    private fun dataObservers(){
        viewModel.apply{
            userDataLiveData.observe(viewLifecycleOwner) {
                updateUiUserData(it)
            }
        }
    }

    private fun updateUiUserData(user : CurrentUserEntity){
        bind?.apply {
            userNick.text = user.userNick
            inpUserName.setText(user.userNick)
            inpUserHeight.setText(user.userHeight.toString())
            inpUserWeight.setText(user.userWeight.toString())
            inpUserAge.setText(user.userBirthday)
        }
    }

    private fun clickBirthdayField(){
        bind?.apply {
            inpUserAge.apply {

            }
        }
    }
}