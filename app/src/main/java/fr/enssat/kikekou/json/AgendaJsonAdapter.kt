package fr.enssat.kikekou.json

import com.squareup.moshi.FromJson

class AgendaJsonAdapter {
    @FromJson
    @AgendaJsonAnnotation
    fun fromJson(@AgendaJsonAnnotation json: String): String {
        var result = json.replace("a value in the json string", "surrogate with a new value")
        return result
    }
}