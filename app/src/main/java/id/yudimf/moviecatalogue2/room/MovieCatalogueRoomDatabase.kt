package id.yudimf.moviecatalogue2.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import id.yudimf.moviecatalogue2.model.Movie
import id.yudimf.moviecatalogue2.room.dao.MovieDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class MovieCatalogueRoomDatabase : RoomDatabase(){
    abstract fun movieDao(): MovieDao

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
//                    var movieDao = database.movieDao()

                    // Delete all content here.
//                    movieDao.deleteAll()

                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: MovieCatalogueRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): MovieCatalogueRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieCatalogueRoomDatabase::class.java,
                    "movie_catalogue_database"
                )
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        fun getDB(context: Context): MovieCatalogueRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        MovieCatalogueRoomDatabase::class.java,
                        "movie_catalogue_database"
                    ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}