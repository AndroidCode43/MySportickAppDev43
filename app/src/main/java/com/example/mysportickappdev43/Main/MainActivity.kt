package com.example.mysportickappdev43.Main

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.example.mysportickappdev43.Auth.activity.AuthActivity
import com.example.mysportickappdev43.R
import com.example.mysportickappdev43.databinding.ActivityMainBinding
import com.example.mysportickappdev43.utils.obj.GpsRunConst.ACTION_SHOW_TRACKING_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var bind : ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind?.root)
        navigateToTrackingFragmentIfNeeded(intent)
        initUi()
    }

    private fun initUi(){

        window.apply {
            statusBarColor = Color.BLACK
        }

    }

    private fun navigateToTrackingFragmentIfNeeded(intent : Intent?){
        if(intent?.action == ACTION_SHOW_TRACKING_FRAGMENT){
            bind?.mainContainer!!.findNavController().navigate(R.id.action_global_startRunFragment)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackingFragmentIfNeeded(intent)
    }
}