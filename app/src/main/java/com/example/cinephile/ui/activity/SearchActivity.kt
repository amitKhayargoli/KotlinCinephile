package com.example.cinephile.ui.activity

import SearchAdapter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinephile.R
import com.example.cinephile.model.MovieModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var searchAdapter: SearchAdapter
    private val movieList = ArrayList<MovieModel>()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        recyclerView = findViewById(R.id.recyclerViewSearchMovies)
        searchEditText = findViewById(R.id.editTextText3)

        recyclerView.layoutManager = LinearLayoutManager(this)
        searchAdapter = SearchAdapter(this, movieList)
        recyclerView.adapter = searchAdapter

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (query.isNotEmpty()) {
                    fetchMovies(query)
                } else {
                    movieList.clear()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun fetchMovies(query: String) {
        val reference: DatabaseReference = database.reference.child("Movies")
        reference.orderByChild("name").startAt(query).endAt(query + "\uf8ff")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    movieList.clear()
                    for (data in snapshot.children) {
                        val movie = data.getValue(MovieModel::class.java)
                        movie?.let {
                            movieList.add(it)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}
