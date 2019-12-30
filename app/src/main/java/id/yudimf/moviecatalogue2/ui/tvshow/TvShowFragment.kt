package id.yudimf.moviecatalogue2.ui.tvshow

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
import id.yudimf.moviecatalogue2.retrofit.response.TvShowResponse
import id.yudimf.moviecatalogue2.ui.detail.DetailActivity
import id.yudimf.moviecatalogue2.ui.adapter.CardViewAdapter
import id.yudimf.moviecatalogue2.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.fragment_tv_show.*
import java.util.*
import kotlin.collections.ArrayList

class TvShowFragment : Fragment() {

    private val list = ArrayList<Movie>()
    private val listTemp = ArrayList<Movie>()
    private val cardViewAdapter = CardViewAdapter(list)
    private lateinit var movieViewModel: MovieViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_tv_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)
        getTvShowsFromRetrofit()
        showRecyclerCardView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tvshow_fragment_menu,menu)
        setSearchQueryListener(menu)
        setSearchExpandListener(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setSearchQueryListener(menu: Menu){
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search_tvshow_fragment).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                var text = query
                text = text.toLowerCase()
                showLoading(true)
                movieViewModel.searchTvShow(text).observe(viewLifecycleOwner, Observer {
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
        val searchMenuItem: MenuItem = menu.findItem(R.id.search_tvshow_fragment)
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

    private fun populateData(list : ArrayList<TvShowResponse>){
        for (tvShowResponse in list){
            val path = "https://image.tmdb.org/t/p/w185"+ tvShowResponse.poster_path
            val desc : String = if (tvShowResponse.description != "") {
                tvShowResponse.description
            } else when (Locale.getDefault().language) {
                "in" -> "Deskripsi tidak tersedia"
                else -> "Description is not availabe"
            }
            val movie = Movie(tvShowResponse._id, 2, tvShowResponse.name, desc, "", tvShowResponse.date, path)
            this.list.add(movie)
        }
    }

    private fun getTvShowsFromRetrofit(){
        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        movieViewModel.listTvShowFromAPI.observe(viewLifecycleOwner, Observer {
            list.clear()
            populateData(it)
            showLoading(false)
            cardViewAdapter.notifyDataSetChanged()
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBarTvShow.visibility = View.VISIBLE
        } else {
            progressBarTvShow.visibility = View.GONE
        }
    }

    private fun showRecyclerCardView() {
        Log.d("rv_movies",rv_tv_show.toString())
        rv_tv_show.setHasFixedSize(true)
        rv_tv_show.layoutManager = LinearLayoutManager(context)
        rv_tv_show.adapter = cardViewAdapter

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

}