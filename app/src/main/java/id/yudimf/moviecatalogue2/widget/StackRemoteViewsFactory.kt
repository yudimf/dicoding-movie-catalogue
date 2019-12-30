package id.yudimf.moviecatalogue2.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.yudimf.moviecatalogue2.R
import id.yudimf.moviecatalogue2.model.Movie
import id.yudimf.moviecatalogue2.room.MovieCatalogueRoomDatabase

class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {

    private val pathList = ArrayList<String>()

    override fun onCreate() {

    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onDataSetChanged() {
        val list : List<Movie> = MovieCatalogueRoomDatabase.getDB(mContext).movieDao().getAll()
        for( item in list){
            pathList.add(item.photoPath)
        }
    }

    override fun hasStableIds(): Boolean = false

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        val bmp = Glide.with(mContext)
            .asBitmap()
            .load(pathList[position])
            .apply(RequestOptions().fitCenter())
            .submit(800,550)
            .get()

        rv.setImageViewBitmap(R.id.imageView, bmp)
        val extras = bundleOf(
            ImageBannerWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getCount(): Int = pathList.size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {

    }
}