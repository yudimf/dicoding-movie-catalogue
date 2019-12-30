package id.yudimf.moviecatalogue2.room.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import id.yudimf.moviecatalogue2.model.Movie

@Dao
interface MovieDao {

    @Query("SELECT * from movie where movie.type_movie = 1")
    fun getAllMovie() : LiveData<List<Movie>>

    @Query("SELECT * from movie where movie.type_movie = 2")
    fun getAllTvShow() : LiveData<List<Movie>>

    @Query("SELECT * from movie where _id = :idMovie")
    fun getMovie(idMovie : Int) : LiveData<List<Movie>>

    @Query("SELECT * from movie")
    fun getAll() : List<Movie>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveMovie(movie: Movie)

    @Delete
    suspend fun delete(movie: Movie)

    @Query("DELETE FROM movie")
    suspend fun deleteAll()

}