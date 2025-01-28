package com.example.cinephile.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cinephile.R
import com.example.cinephile.databinding.ActivitySignupBinding
import com.example.cinephile.model.UserModel
import com.example.cinephile.repository.UserRepositoryImpl
import com.example.cinephile.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding
    lateinit var auth: FirebaseAuth
    lateinit var userViewModel: UserViewModel

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val reference: DatabaseReference = database.reference.child("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userRepository = UserRepositoryImpl()

        userViewModel = UserViewModel(userRepository)


        auth = FirebaseAuth.getInstance()

            binding.SignupBtn.setOnClickListener {

                var email: String = binding.SignupEmail.text.toString()
                var password: String = binding.SignupPassword.text.toString()


                userViewModel.signup(email, password) { success, message, userId ->
                    if (success) {
                        val userModel = UserModel(
                            userId.toString(),
                            email
                        )
                        addUser(userModel)


                    } else {

                        Toast.makeText(
                            this@SignupActivity,
                            message, Toast.LENGTH_SHORT
                        ).show()
                    }


                }
            }

            binding.SignupActLoginBtn.setOnClickListener{
                val intent = Intent(this@SignupActivity,
                    LoginActivity::class.java)
                startActivity(intent)
            }

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.SignupActivity)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

    }

    fun addUser(userModel: UserModel) {
        userViewModel.addUserToDatabase(userModel.userId, userModel) { success, message ->
            if (success) {
                Toast.makeText(
                    this@SignupActivity, message, Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this@SignupActivity, message, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}

