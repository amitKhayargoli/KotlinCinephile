package com.example.cinephile.repository

import com.example.cinephile.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserRepositoryImpl : UserRepository {
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    val database : FirebaseDatabase = FirebaseDatabase.getInstance()

    val reference: DatabaseReference = database.reference.child("users")
    override fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
            if(it.isSuccessful){
                callback(true,"Login Successful");
            }else{
                callback(false,it.exception?.message.toString())
            }
        }
    }

    override fun signup(
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
            if(it.isSuccessful){
                callback(true,"Registration Successful",auth.currentUser?.uid.toString())
            }
            else{
                callback(false,it.exception?.message.toString(),"")
            }
        }
    }

    override fun forgetPassword(email: String, callback: (Boolean, String) -> Unit) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener{
            if(it.isSuccessful){
                callback(true,"Reset Email Sent Successfully")
            }
            else{
                callback(false,it.exception?.message.toString());

            }
        }
    }

    override fun addUserToDatabase(
        userId: String,
        userModel: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        reference.child(userId).setValue(userModel).addOnCompleteListener{
            if(it.isSuccessful){
                callback(true,"Registration Successful")
            }else{
                callback(false,it.exception?.message.toString())
            }
        }
    }


    override fun getUserFromDatabase(
        userId: String,
        callback: (UserModel?, Boolean, String) -> Unit
    ) {
        reference.child(userId).addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    var model = snapshot.getValue(UserModel::class.java)

                    callback(model,true,"Details fetched successfully")
                }
            }

            override fun onCancelled(error: DatabaseError) {
             callback(null,false,error.message)
            }

        })


    }

    override fun logout(callback: (Boolean, String) -> Unit) {
        try{
            auth.signOut()
            callback(true,"Signout successful")

        }
        catch (e:Exception){
            callback(false,e.message.toString())
        }
    }






}