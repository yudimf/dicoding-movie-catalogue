package id.yudimf.moviecatalogue2.retrofit

import id.yudimf.moviecatalogue2.retrofit.response.MovieListResponse
import id.yudimf.moviecatalogue2.retrofit.response.TvShowListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIServices {

    @GET("discover/movie")
    fun getAllMovie(@Query("api_key") API_KEY : String, @Query("language") language : String) : Call<MovieListResponse>

    @GET("discover/tv")
    fun getAllTvShow(@Query("api_key") API_KEY : String, @Query("language") language : String) : Call<TvShowListResponse>

    @GET("search/movie")
    fun searchMovies(@Query("api_key") API_KEY : String, @Query("language") language : String, @Query("query") query : String ) : Call<MovieListResponse>

    @GET("search/tv")
    fun searchTvShow(@Query("api_key") API_KEY : String, @Query("language") language : String, @Query("query") query : String ) : Call<TvShowListResponse>
}