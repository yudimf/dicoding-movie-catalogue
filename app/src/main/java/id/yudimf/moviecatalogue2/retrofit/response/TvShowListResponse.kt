package id.yudimf.moviecatalogue2.retrofit.response

import com.google.gson.annotations.SerializedName

data class TvShowListResponse (
    @SerializedName("results")
    val tvShows: ArrayList<TvShowResponse>

)