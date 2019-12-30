package id.yudimf.moviecatalogue2.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.yudimf.moviecatalogue2.BuildConfig
import id.yudimf.moviecatalogue2.model.Movie
import id.yudimf.moviecatalogue2.retrofit.RetrofitClient
import id.yudimf.moviecatalogue2.retrofit.response.MovieListResponse
import id.yudimf.moviecatalogue2.retrofit.response.MovieResponse
import id.yudimf.moviecatalogue2.retrofit.response.TvShowListResponse
import id.yudimf.moviecatalogue2.retrofit.response.TvShowResponse
import id.yudimf.moviecatalogue2.room.dao.MovieDao
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class MovieRepository(private val movieDao: MovieDao) {

    val allMovie : LiveData<List<Movie>> = movieDao.getAllMovie()
    val allTvShow : LiveData<List<Movie>> = movieDao.getAllTvShow()

    fun getMoviesFromAPI() : MutableLiveData<ArrayList<MovieResponse>>{
        val moviesFromAPI = MutableLiveData<ArrayList<MovieResponse>>()
        RetrofitClient().services.getAllMovie(BuildConfig.THE_MOVIE_DB_API_TOKEN, getLocaleLanguage()).enqueue(object : Callback<MovieListResponse>{
            override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                Log.d("get from API","gagal")
                moviesFromAPI.postValue(null)
            }

            override fun onResponse(call: Call<MovieListResponse>, response: Response<MovieListResponse>) {
                if (response.isSuccessful){
                    moviesFromAPI.postValue(response.body()?.movies)
                }
            }

        })
        return moviesFromAPI
    }

    fun getTvShowsFromAPI() : MutableLiveData<ArrayList<TvShowResponse>> {
        val tvShowsFromAPI = MutableLiveData<ArrayList<TvShowResponse>>()
        RetrofitClient().services.getAllTvShow(BuildConfig.THE_MOVIE_DB_API_TOKEN,getLocaleLanguage()).enqueue(object : Callback<TvShowListResponse>{
            override fun onFailure(call: Call<TvShowListResponse>, t: Throwable) {
                tvShowsFromAPI.postValue(null)
                Log.d("test","gagal")
            }

            override fun onResponse(call: Call<TvShowListResponse>,response: Response<TvShowListResponse>) {
                if (response.isSuccessful){
                    tvShowsFromAPI.postValue(response.body()?.tvShows)
                }
            }
        })
        return tvShowsFromAPI
    }

    fun searchTvShow(tvShowName : String) : MutableLiveData<ArrayList<TvShowResponse>> {
        val tvShowsFromAPI = MutableLiveData<ArrayList<TvShowResponse>>()
        RetrofitClient().services.searchTvShow(BuildConfig.THE_MOVIE_DB_API_TOKEN,getLocaleLanguage(),tvShowName).enqueue(object : Callback<TvShowListResponse>{
            override fun onFailure(call: Call<TvShowListResponse>, t: Throwable) {
                tvShowsFromAPI.postValue(null)
                Log.d("test","gagal")
            }

            override fun onResponse(call: Call<TvShowListResponse>,response: Response<TvShowListResponse>) {
                if (response.isSuccessful){
                    tvShowsFromAPI.postValue(response.body()?.tvShows)
                }
            }
        })
        return tvShowsFromAPI
    }

    fun searchMovies(movieName : String) : MutableLiveData<ArrayList<MovieResponse>> {
        val moviesFromAPI = MutableLiveData<ArrayList<MovieResponse>>()
        RetrofitClient().services.searchMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN,getLocaleLanguage(),movieName).enqueue(object : Callback<MovieListResponse>{
            override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                Log.d("get from API","gagal")
                moviesFromAPI.postValue(null)
            }

            override fun onResponse(
                call: Call<MovieListResponse>,
                response: Response<MovieListResponse>
            ) {
                if (response.isSuccessful){
                    moviesFromAPI.postValue(response.body()?.movies)
                }
            }

        })
        return moviesFromAPI
    }

    fun selectMovie(idMovie : Int) : LiveData<List<Movie>>{
        return movieDao.getMovie(idMovie)
    }

    suspend fun insert(movie: Movie){
        movieDao.saveMovie(movie)
    }

    suspend fun delete(movie: Movie){
        movieDao.delete(movie)
    }

    private fun getLocaleLanguage() : String{
        return when (Locale.getDefault().language) {
            "in" -> {"id"}
            else -> {"en"}
        }
    }

}