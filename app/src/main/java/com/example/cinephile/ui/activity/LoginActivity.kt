package com.example.cinephile.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cinephile.MainActivity
import com.example.cinephile.R
import com.example.cinephile.databinding.ActivityLoginBinding
import com.example.cinephile.repository.UserRepositoryImpl
import com.example.cinephile.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var auth: FirebaseAuth
    lateinit var userViewModel: UserViewModel


    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val reference: DatabaseReference = database.reference.child("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userRepository = UserRepositoryImpl()

        userViewModel = UserViewModel(userRepository)

        binding.LoginBtn.setOnClickListener{
            var email : String = binding.LoginEmail.text.toString();
            var password : String = binding.LoginPassword.text.toString()

            userViewModel.login(email,password){
                    success,message->
                if(success){
                    val intent = Intent(this@LoginActivity,
                        NavigationActivity::class.java)
                    startActivity(intent)
                }else {
                    Toast.makeText(
                        this@LoginActivity,
                        message, Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }


        binding.LoginActSignupBtn.setOnClickListener{
            val intent = Intent(this@LoginActivity,
                SignupActivity::class.java)
            startActivity(intent)
        }


        binding.LoginForgetPass.setOnClickListener{
            val intent = Intent(this@LoginActivity,
                ForgetPasswordActivity::class.java)
            startActivity(intent)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.LoginActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}