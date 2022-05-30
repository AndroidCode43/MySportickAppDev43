package com.example.mysportickappdev43.Main.fragments.home

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.mysportickappdev43.Main.fragments.home.ExecRecyclerView.Companion.recyclerClickListener
import com.example.mysportickappdev43.R
import com.example.mysportickappdev43.databinding.FragmentHomeBinding
import com.example.mysportickappdev43.room.entity.CreatedExerciseEntity
import com.example.mysportickappdev43.room.entity.CurrentUserEntity
import com.example.mysportickappdev43.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) , RecyclerClickListener{

    private lateinit var mBinding : FragmentHomeBinding
    private val viewModel : HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentHomeBinding.bind(view)
        init()
    }

    private fun init(){
        lifecycle.addObserver(viewModel)
        initUi()
        observersData()
    }

    private fun initUi(){
        btnOpenDrawer()
    }

    private fun observersData(){
        viewModel.getAllExerciseLiveData.observe(viewLifecycleOwner) {
            //init recycler view
            mBinding.execRecycler.adapter = ExecRecyclerView(it,resources).apply {
                recyclerClickListener = this@HomeFragment
            }
        }

        //current user observer
        viewModel.currentUserLiveData.observe(viewLifecycleOwner){
            mBinding.apply {
                initUserTextFields(it)
            }
        }
    }

    private fun btnOpenDrawer(){
        mBinding.apply { 
            btnOpenDrawer.setOnClickListener {
                mainDrawer.openDrawer(GravityCompat.START)

                    mainNavigation.setNavigationItemSelectedListener{
                        when(it.itemId){
                            R.id.menu_nav_settings -> {
                                startNavigation(R.id.action_homeFragment_to_settingsFragment)
                                mainDrawer.closeDrawer(GravityCompat.START)
                            }

                            R.id.menu_nav_create -> {
                                startNavigation(R.id.action_homeFragment_to_fragmentCreateExec)
                                mainDrawer.closeDrawer(GravityCompat.START)
                            }

                            R.id.menu_nav_start_run -> {
                                startNavigation(R.id.action_homeFragment_to_startRunFragment)
                                mainDrawer.closeDrawer(GravityCompat.START)
                            }
                        }
                        false
                    }

            }
        }
    }

    override fun clickItem(item: Any) {
        when(item){
            START_CREATE_EXEC -> startNavigation(R.id.action_homeFragment_to_fragmentCreateExec)

            is CreatedExerciseEntity -> startNavigationWithBundle(
                R.id.action_homeFragment_to_currentExercise,
                Bundle().apply {
                    putSerializable(KEY_CURRENT_EXERCISE_ID, item)
                }
            )
        }

    }

    private fun initUserTextFields(userData : CurrentUserEntity){
        mBinding.apply {
            txHelloPlus.text = "${resources.getText(R.string.hello)} ${userData.userNick}!"
            txUserWeight.text = userData.userWeight.toString()
            txUserYears.text = userData.userBirthday
        }
    }
}