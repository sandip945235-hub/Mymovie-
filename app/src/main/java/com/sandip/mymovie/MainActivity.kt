package com.sandip.mymovie

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MovieAdapter
    private lateinit var progressBar: ProgressBar

    private val apiRepository = ApiRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.movieRecyclerView)
        progressBar = findViewById(R.id.progressBar)

        recyclerView.layoutManager = GridLayoutManager(this, 3)

        adapter = MovieAdapter(emptyList()) { movie ->

            if (movie.embedLink.isBlank()) {

                Toast.makeText(
                    this,
                    "इस movie का video link उपलब्ध नहीं है",
                    Toast.LENGTH_SHORT
                ).show()

                return@MovieAdapter
            }

            val intent = Intent(
                this,
                PlayerActivity::class.java
            )

            intent.putExtra("title", movie.title)
            intent.putExtra("video_url", movie.embedLink)

            startActivity(intent)
        }

        recyclerView.adapter = adapter

        loadMovies()
    }

    private fun loadMovies() {

        progressBar.visibility = View.VISIBLE

        apiRepository.getMovies(

            onSuccess = { movies ->

                runOnUiThread {

                    progressBar.visibility = View.GONE

                    adapter.updateMovies(movies)

                    if (movies.isEmpty()) {

                        Toast.makeText(
                            this,
                            "कोई movie नहीं मिली",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            },

            onError = { error ->

                runOnUiThread {

                    progressBar.visibility = View.GONE

                    Toast.makeText(
                        this,
                        error,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }
}
