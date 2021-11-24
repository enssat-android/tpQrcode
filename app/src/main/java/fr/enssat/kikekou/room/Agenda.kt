package fr.enssat.kikekou.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Embedded

class Location(var day:Int, var place:String)
class Contact(var mail:String, var tel:String?, var fb:String?)

@Entity(tableName = "agenda_table")
class Agenda(@ColumnInfo(name = "name") var name:String,
             @ColumnInfo(name = "week") var week:Int,
             @Embedded
             var loc: Location,
             @Embedded
             var contact:Contact) {
    @PrimaryKey(autoGenerate = true)
    private var id:Int = 0
        fun getId():Int {
            return id
        }
        fun  setId(value:Int){
            id = value
        }
}