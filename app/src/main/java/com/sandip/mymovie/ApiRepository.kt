package com.sandip.mymovie

import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.Executors

class ApiRepository {

    companion object {
        private const val API_URL =
            "https://my-csv-api.sandip945235.workers.dev/"
    }

    private val client = OkHttpClient()
    private val executor = Executors.newSingleThreadExecutor()

    fun getMovies(
        onSuccess: (List<Movie>) -> Unit,
        onError: (String) -> Unit
    ) {

        executor.execute {

            try {

                val request = Request.Builder()
                    .url(API_URL)
                    .get()
                    .build()

                client.newCall(request).execute().use { response ->

                    if (!response.isSuccessful) {
                        throw Exception(
                            "Server error: ${response.code}"
                        )
                    }

                    val csv = response.body?.string()
                        ?: throw Exception("CSV data खाली है")

                    val movies = parseCsv(csv)

                    onSuccess(movies)
                }

            } catch (e: Exception) {

                onError(
                    e.message ?: "Data load नहीं हो पाया"
                )
            }
        }
    }

    private fun parseCsv(csv: String): List<Movie> {

        val result = mutableListOf<Movie>()

        val lines = csv
            .trim()
            .lines()
            .filter { it.isNotBlank() }

        if (lines.size <= 1) {
            return result
        }

        for (i in 1 until lines.size) {

            val columns = parseCsvLine(lines[i])

            if (columns.size >= 5) {

                val title = columns[0].trim()
                val poster = columns[1].trim()
                val category = columns[2].trim()
                val embedLink = columns[3].trim()
                val downloadLink = columns[4].trim()

                result.add(
                    Movie(
                        title = title,
                        poster = poster,
                        category = category,
                        embedLink = embedLink,
                        downloadLink = downloadLink
                    )
                )
            }
        }

        return result
    }

    private fun parseCsvLine(line: String): List<String> {

        val values = mutableListOf<String>()
        val current = StringBuilder()

        var insideQuotes = false

        for (char in line) {

            when {

                char == '"' -> {
                    insideQuotes = !insideQuotes
                }

                char == ',' && !insideQuotes -> {
                    values.add(current.toString())
                    current.clear()
                }

                else -> {
                    current.append(char)
                }
            }
        }

        values.add(current.toString())

        return values
    }
}
