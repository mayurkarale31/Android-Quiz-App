package com.codevelop.androidquizapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codevelop.androidquizapp.databinding.UserInfoActivityBinding

class UserInfoActivity : AppCompatActivity() {

    private lateinit var binding : UserInfoActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserInfoActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.okButton.setOnClickListener {
            val username = binding.edtUserName.text.toString()

            if (username.isEmpty()) {
                // Change the hint color to red
                //binding.edtUserName.setHintTextColor(Color.RED)
                binding.txtInput.helperText = "Please Enter Your Name"
            } else {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("username", username)
                startActivity(intent)
            }
        }
    }
}