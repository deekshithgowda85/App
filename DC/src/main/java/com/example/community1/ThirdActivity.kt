package com.example.community1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import android.util.Log
import android.content.Intent
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class ThirdActivity : AppCompatActivity() {

    private lateinit var  buttomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2) // Use XML layout {

        buttomNavigationView = findViewById(R.id.buttomnavg)
        buttomNavigationView.setOnItemSelectedListener { menuItem->
            when(menuItem.itemId){
                R.id.buttom_home->{
                replaceFragment(HomeFragment())
                true
            }
                R.id.buttom_chat->{
                    replaceFragment(ChatFragment())
                    true
                }
                R.id.buttom_box->{
                    replaceFragment(ShortsFragment())
                    true
                }
                R.id.buttom_account->{
                    replaceFragment(ProfileFragment())
                    true
                }
                else ->false
            }
        }
        replaceFragment(HomeFragment())
    }
    private fun replaceFragment(fragment:Fragment){

        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }
}