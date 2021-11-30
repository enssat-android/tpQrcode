package fr.enssat.kikekou.room

import androidx.room.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import fr.enssat.kikekou.json.ListLocationConverter

@JsonClass(generateAdapter = true)
data class Location(var day:Int, var place:String)

@JsonClass(generateAdapter = true)
data class Contact(var mail:String, var tel:String?, var fb:String?)

@Entity(tableName = "agenda_table")
@JsonClass(generateAdapter = true)
data class Agenda(
    @ColumnInfo(name = "name")
    @field:Json(name = "name") var name:String,

    @ColumnInfo(name = "week")
    @field:Json(name = "week") var week:Int,

    @TypeConverters(ListLocationConverter::class)
    @field:Json(name = "loc") var loc: List<Location>,

    @Embedded
    @field:Json(name = "contacts") var contact:Contact) {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    @Transient
    private var id:Int = 0
        fun getId():Int {
            return id
        }
        fun  setId(value:Int){
            id = value
        }
}