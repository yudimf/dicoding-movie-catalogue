package id.yudimf.moviecatalogue2.ui.movie

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.yudimf.moviecatalogue2.R
import id.yudimf.moviecatalogue2.model.Movie
import id.yudimf.moviecatalogue2.retrofit.response.MovieResponse
import id.yudimf.moviecatalogue2.ui.detail.DetailActivity
import id.yudimf.moviecatalogue2.ui.adapter.CardViewAdapter
import id.yudimf.moviecatalogue2.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.fragment_movie.*
import java.util.*
import kotlin.collections.ArrayList

class MovieFragment : Fragment() {

    private val list = ArrayList<Movie>()
    private val listTemp = ArrayList<Movie>()
    private val cardViewAdapter = CardViewAdapter(list)
    private lateinit var movieViewModel: MovieViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)
        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        getMovieFromRetrofit()
        showRecyclerCardView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.movie_fragment_menu,menu)
        setSearchQueryListener(menu)
        setSearchExpandListener(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun getMovieFromRetrofit(){
        movieViewModel.listMovieFromAPI.observe(viewLifecycleOwner, Observer {
            list.clear()
            populateData(it)
            showLoading(false)
            cardViewAdapter.notifyDataSetChanged()
        })
    }

    private fun populateData(listMovie: ArrayList<MovieResponse>){
        for (movieResponse in listMovie){
            val path = "https://image.tmdb.org/t/p/w185"+ movieResponse.poster_path
            val desc : String = if (movieResponse.description != "") {
                movieResponse.description
            } else {
                when (Locale.getDefault().language) {
                    "in" -> "Deskripsi tidak tersedia"
                    else -> "Description is not availabe"
                }
            }
            val movie = Movie(movieResponse._id,1 , movieResponse.name, desc, "", movieResponse.date, path)
            list.add(movie)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBarMovie.visibility = View.VISIBLE
        } else {
            progressBarMovie.visibility = View.GONE
        }
    }

    private fun showRecyclerCardView() {
        Log.d("rv_movies",rv_movies.toString())
        rv_movies.setHasFixedSize(true)
        rv_movies.layoutManager = LinearLayoutManager(context)
        rv_movies.adapter = cardViewAdapter

        cardViewAdapter.setOnItemClickCallback(object : CardViewAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Movie) {
                showSelectedMovie(data)
            }
        })
    }

    private fun showSelectedMovie(movie: Movie) {
        val intent = Intent(context,DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_MOVIE,movie)
        startActivity(intent)
    }

    private fun setSearchQueryListener(menu: Menu){
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search_movie_fragment).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                var text = query
                text = text.toLowerCase()
                showLoading(true)
                movieViewModel.searchMovie(text).observe(viewLifecycleOwner, Observer {
                    listTemp.addAll(list)
                    list.clear()
                    populateData(it)
                    showLoading(false)
                    cardViewAdapter.notifyDataSetChanged()
                })
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    private fun setSearchExpandListener(menu: Menu){
        val searchMenuItem: MenuItem = menu.findItem(R.id.search_movie_fragment)
        searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                if (listTemp.isNotEmpty()){
                    list.clear()
                    list.addAll(listTemp)
                    cardViewAdapter.notifyDataSetChanged()
                }
                return true
            }
        })
    }

}