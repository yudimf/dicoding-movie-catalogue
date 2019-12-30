package id.yudimf.moviecatalogue2.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "movie")
data class Movie (
    @PrimaryKey
    @ColumnInfo
    val _id: Int?,

    @ColumnInfo
    val type_movie: Int,

    @ColumnInfo
    var name: String,

    @ColumnInfo
    var description: String,

    @ColumnInfo
    var description_in: String,

    @ColumnInfo
    var date: String,

    @ColumnInfo
    var photoPath : String
) : Parcelable