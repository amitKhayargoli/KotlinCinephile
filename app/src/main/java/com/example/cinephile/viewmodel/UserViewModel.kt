package com.example.cinephile.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.cinephile.model.UserModel
import com.example.cinephile.repository.UserRepository
import com.google.firebase.auth.FirebaseUser

class UserViewModel(val repo:UserRepository) {
    fun login(email: String, password: String, callback:(Boolean, String)->Unit){
        repo.login(email,password,callback);
    }

    fun signup(email: String, password: String, callback: (Boolean, String, Any?) -> Unit){
        repo.signup(email,password,callback);
    }

    fun forgotPassword(email: String, callback: (Boolean, String) -> Unit){
        repo.forgetPassword(email, callback);
    }

    fun addUserToDatabase(userId: String, userModel: UserModel, callback: (Boolean, String) -> Unit){
        repo.addUserToDatabase(userId,userModel,callback);
    }


    var _userData = MutableLiveData<UserModel?>()

    var userData = MutableLiveData<UserModel?>()

        get() = _userData


    fun getUserFromDatabase(userId: String){
        repo.getUserFromDatabase(userId){
                userModel,success,message->
            if(success){
                _userData.value = userModel
            }
            else{

                _userData.value = null
            }
        }

    }

    fun logout(callback: (Boolean, String) -> Unit) {
        repo.logout(callback)
    }


}