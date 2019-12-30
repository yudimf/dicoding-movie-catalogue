package id.yudimf.moviecatalogue2.ui.favorite


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import id.yudimf.moviecatalogue2.R
import id.yudimf.moviecatalogue2.model.Movie
import id.yudimf.moviecatalogue2.ui.adapter.CardViewAdapter
import id.yudimf.moviecatalogue2.ui.detail.DetailActivity
import id.yudimf.moviecatalogue2.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.fragment_favorite_movie.*
import kotlinx.android.synthetic.main.fragment_movie.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class FavoriteMovieFragment : Fragment() {

    private val list = ArrayList<Movie>()
    private val cardViewAdapter = CardViewAdapter(list)
    private lateinit var movieViewModel: MovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        movieViewModel.allMovie.observe(viewLifecycleOwner, Observer {
            it?.let {
                list.clear()
                list.addAll(it)
                showLoading(false)
                cardViewAdapter.notifyDataSetChanged()
            }
        })

        rv_favorite_movies.setHasFixedSize(true)
        rv_favorite_movies.layoutManager = LinearLayoutManager(context)
        rv_favorite_movies.adapter = cardViewAdapter

        cardViewAdapter.setOnItemClickCallback(object : CardViewAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Movie) {
                showSelectedMovie(data)
            }
        })

        super.onViewCreated(view, savedInstanceState)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBarFavoriteMovie.visibility = View.VISIBLE
        } else {
            progressBarFavoriteMovie.visibility = View.GONE
        }
    }

    private fun showSelectedMovie(movie: Movie) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_MOVIE,movie)
        startActivity(intent)
    }

}
