package id.yudimf.moviecatalogue2.viewmodel

import android.app.Application
import androidx.lifecycle.*
import id.yudimf.moviecatalogue2.model.Movie
import id.yudimf.moviecatalogue2.repository.MovieRepository
import id.yudimf.moviecatalogue2.retrofit.response.MovieResponse
import id.yudimf.moviecatalogue2.retrofit.response.TvShowResponse
import id.yudimf.moviecatalogue2.room.MovieCatalogueRoomDatabase
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class MovieViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : MovieRepository

    var listMovieFromAPI = MutableLiveData<ArrayList<MovieResponse>>()
    var listTvShowFromAPI = MutableLiveData<ArrayList<TvShowResponse>>()

    val allMovie : LiveData<List<Movie>>
    val allTvShow : LiveData<List<Movie>>
    lateinit var movieSelected : LiveData<List<Movie>>

    init {
        val movieDao = MovieCatalogueRoomDatabase.getDatabase(application, viewModelScope).movieDao()
        repository = MovieRepository(movieDao)
        allMovie = repository.allMovie
        allTvShow = repository.allTvShow

        listMovieFromAPI = repository.getMoviesFromAPI()
        listTvShowFromAPI = repository.getTvShowsFromAPI()
    }

    fun searchTvShow(tvShowName : String) : MutableLiveData<ArrayList<TvShowResponse>>{
        return repository.searchTvShow(tvShowName)
    }

    fun searchMovie(movieName : String) : MutableLiveData<ArrayList<MovieResponse>>{
        return repository.searchMovies(movieName)
    }

    fun selectMovie(idMovie : Int) = viewModelScope.launch {
        movieSelected = repository.selectMovie(idMovie)
    }

    fun insertMovie(movie: Movie) = viewModelScope.launch{
        repository.insert(movie)
    }

    fun deleteMovie(movie: Movie) = viewModelScope.launch {
        repository.delete(movie)
    }

}