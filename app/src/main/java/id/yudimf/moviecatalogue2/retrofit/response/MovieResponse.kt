package id.yudimf.moviecatalogue2.retrofit.response

import com.google.gson.annotations.SerializedName

data class MovieResponse(

    @SerializedName("id")
    val _id: Int,

    @SerializedName("poster_path")
    val poster_path: String,

    @SerializedName("title")
    val name: String,

    @SerializedName("overview")
    val description: String,

    @SerializedName("release_date")
    val date: String

)
