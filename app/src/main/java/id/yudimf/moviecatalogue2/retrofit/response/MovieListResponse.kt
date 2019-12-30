package id.yudimf.moviecatalogue2.retrofit.response

import com.google.gson.annotations.SerializedName

data class MovieListResponse (
    @SerializedName("results")
    val movies: ArrayList<MovieResponse>
)