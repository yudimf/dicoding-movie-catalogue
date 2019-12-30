package id.yudimf.moviecatalogue2.retrofit.response

import com.google.gson.annotations.SerializedName

data class TvShowResponse (

    @SerializedName("id")
    val _id: Int,

    @SerializedName("poster_path")
    val poster_path: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("overview")
    val description: String,

    @SerializedName("first_air_date")
    val date: String

)