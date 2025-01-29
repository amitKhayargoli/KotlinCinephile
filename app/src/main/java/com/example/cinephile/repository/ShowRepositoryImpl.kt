package com.example.cinephile.repository
import com.example.cinephile.model.ShowModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ShowRepositoryImpl:ShowRepository {

    val database : FirebaseDatabase = FirebaseDatabase.getInstance()

    val reference : DatabaseReference = database.reference.child("Shows")


    override fun addShows(ShowModel: ShowModel, callback: (Boolean, String) -> Unit) {
        var id = reference.push().key.toString()
        ShowModel.showId = id

        reference.child(id).setValue(ShowModel).addOnCompleteListener{
            if(it.isSuccessful){
                callback(true,"Added Successfully")

            }else{
                callback(false,"${it.exception?.message}")
            }

        }
    }

    override fun updateShow(
        ShowId: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    ) {
        reference.child(ShowId).updateChildren(data).addOnCompleteListener{
            if(it.isSuccessful){
                callback(true,"Product Added Successfully")

            }else{
                callback(false,"${it.exception?.message}")
            }
        }
    }

    override fun deleteShow(ShowId: String, callback: (Boolean, String) -> Unit) {
        reference.child(ShowId).removeValue().addOnCompleteListener{
            if(it.isSuccessful){
                callback(true,"Product Added Successfully")

            }else{
                callback(false,"${it.exception?.message}")
            }

        }
    }

    override fun getShowById(ShowId: String, callback: (ShowModel?, Boolean, String) -> Unit) {
        reference.child(ShowId).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    var model = snapshot.getValue(ShowModel::class.java)

                    callback(model,true,"Data fetched")
                }
            }

            override fun onCancelled(error: DatabaseError) {

                callback(null,false,error.message.toString())
            }

        })



    }

    override fun getAllShows(callback: (List<ShowModel>?, Boolean, String) -> Unit) {
        reference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var Shows = mutableListOf<ShowModel>();

                if(snapshot.exists()){
                    for(Show in snapshot.children){
                        var model = Show.getValue(ShowModel::class.java)

                        if(model!=null){
                            Shows.add(model)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null,false,error.message.toString())

            }

        })
    }
}