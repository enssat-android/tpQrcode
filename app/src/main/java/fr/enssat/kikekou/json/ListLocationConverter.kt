package fr.enssat.kikekou.json

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import fr.enssat.kikekou.room.Location

class ListLocationConverter {
    private val moshi = Moshi.Builder().build()
    private val locType = Types.newParameterizedType(List::class.java, Location::class.java)
    private val locAdapter = moshi.adapter<List<Location>>(locType)

    @TypeConverter
    fun stringToLocations(string: String): List<Location> {
        return locAdapter.fromJson(string).orEmpty()
    }

    @TypeConverter
    fun LocationsToString(loc: List<Location>): String {
        return locAdapter.toJson(loc)
    }

}