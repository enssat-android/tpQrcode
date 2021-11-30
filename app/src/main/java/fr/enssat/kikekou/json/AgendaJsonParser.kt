package fr.enssat.kikekou.json

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import fr.enssat.kikekou.room.Agenda

class AgendaJsonParser {

    companion object {
        fun parseAgenda(json: String): Agenda? {
            val moshi = Moshi.Builder().build()
            val adapter: JsonAdapter<Agenda> = moshi.adapter(Agenda::class.java)
            return adapter.fromJson(json)
        }
    }

}