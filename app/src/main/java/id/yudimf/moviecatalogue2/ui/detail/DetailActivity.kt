package id.yudimf.moviecatalogue2.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.CompoundButton
import android.widget.Toast
import android.widget.ToggleButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.yudimf.moviecatalogue2.R
import id.yudimf.moviecatalogue2.model.Movie
import id.yudimf.moviecatalogue2.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var movieSelected : Movie
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var menuAddToFavorite : MenuItem
    private lateinit var menuRemoveFromFavorite : MenuItem


    companion object {
        const val EXTRA_MOVIE = "extra_movie"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        movieSelected = intent.getParcelableExtra(EXTRA_MOVIE) as Movie
        movie_name_detail.text = movieSelected.name
        movie_date_detail.text = movieSelected.date
        movie_overview_detail.text = movieSelected.description
        Glide.with(this)
            .load(movieSelected.photoPath)
            .placeholder(R.drawable.ic_placeholder_image_24dp)
            .error(R.drawable.ic_broken_image_24dp)
            .apply(RequestOptions().override(200,300))
            .into(img_photo_detail)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_menu,menu)
        menuAddToFavorite = menu.findItem(R.id.add_to_favorite)
        menuRemoveFromFavorite = menu.findItem(R.id.remove_from_favorite)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.add_to_favorite -> {
                movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
                movieViewModel.insertMovie(movieSelected)
                menuAddToFavorite.isVisible = false
                menuRemoveFromFavorite.isVisible = true
                Toast.makeText(this,"Added to Favorite",Toast.LENGTH_SHORT).show()
            }
            R.id.remove_from_favorite -> {
                movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
                movieViewModel.deleteMovie(movieSelected)
                menuAddToFavorite.isVisible = true
                menuRemoveFromFavorite.isVisible = false
                Toast.makeText(this,"Remove from Favorite",Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        movieSelected._id?.let { movieViewModel.selectMovie(it) }
        movieViewModel.movieSelected.observe(this, Observer {
            when {
                it.isEmpty() -> {
                    menuAddToFavorite.isVisible = true
                    menuRemoveFromFavorite.isVisible = false
                }
                else -> {
                    menuAddToFavorite.isVisible = false
                    menuRemoveFromFavorite.isVisible = true
                }
            }
        })

        return super.onPrepareOptionsMenu(menu)
    }

}
