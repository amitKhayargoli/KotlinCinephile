package com.example.cinephile.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cinephile.R
import com.example.cinephile.databinding.ActivityForgetPasswordBinding
import com.example.cinephile.repository.UserRepositoryImpl
import com.example.cinephile.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ForgetPasswordActivity : AppCompatActivity() {

    lateinit var binding: ActivityForgetPasswordBinding
    lateinit var auth: FirebaseAuth
    lateinit var userViewModel: UserViewModel


    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val reference: DatabaseReference = database.reference.child("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.resetBtn.setOnClickListener{
            var email : String = binding.forgetEmail.text.toString();

            userViewModel.forgotPassword(email){
                    success,message->
                if(success){
                    val intent = Intent(this@ForgetPasswordActivity,
                        LoginActivity::class.java)
                    startActivity(intent)
                }
                else {
                    Toast.makeText(
                        this@ForgetPasswordActivity,
                        message, Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }




        val userRepository = UserRepositoryImpl()

        userViewModel = UserViewModel(userRepository)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}