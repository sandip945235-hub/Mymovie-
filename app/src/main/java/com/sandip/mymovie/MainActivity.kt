package com.sandip.mymovie

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.movieRecyclerView)

        recyclerView.layoutManager = GridLayoutManager(this, 3)

        adapter = MovieAdapter(emptyList()) { movie ->
            if (movie.embedLink.isNotBlank()) {
                val intent = Intent(this, PlayerActivity::class.java)

                intent.putExtra("title", movie.title)
                intent.putExtra("video_url", movie.embedLink)

                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    "Video link उपलब्ध नहीं है",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        recyclerView.adapter = adapter

        loadMovies()
    }

    private fun loadMovies() {

        // अभी temporary data है।
        // अगले step में Cloudflare API से असली data आएगा।

        val movies = listOf(
            Movie(
                title = "Sample Movie",
                poster = "",
                category = "Bollywood",
                embedLink = "",
                downloadLink = ""
            )
        )

        adapter.updateMovies(movies)
    }
}
