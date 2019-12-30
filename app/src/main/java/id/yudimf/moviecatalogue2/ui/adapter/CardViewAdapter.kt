package id.yudimf.moviecatalogue2.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.yudimf.moviecatalogue2.model.Movie
import id.yudimf.moviecatalogue2.R
import kotlinx.android.synthetic.main.item_row_movie_tv_show.view.*

class CardViewAdapter(private val listMovie: ArrayList<Movie>) : RecyclerView.Adapter<CardViewAdapter.CardViewViewHolder>() {

    private var list : ArrayList<Movie> = listMovie

    private var onItemClickCallback : OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Movie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_movie_tv_show,parent,false)
        return CardViewViewHolder(view)
    }

    override fun getItemCount(): Int  = list.size

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class CardViewViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(movie.photoPath)
                    .placeholder(R.drawable.ic_placeholder_image_24dp)
                    .error(R.drawable.ic_broken_image_24dp)
                    .apply(RequestOptions().override(200,200))
                    .into(img_photo)
                txt_name.text = movie.name
                txt_date.text = movie.date
                txt_overview.text = movie.description
                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(movie)
                }
            }
        }
    }

    fun filter(listMovie: ArrayList<Movie>){
        list.clear()
        list.addAll(listMovie)
        notifyDataSetChanged()
    }
}