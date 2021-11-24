package fr.enssat.kikekou.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.enssat.kikekou.R


class AgendaViewHolder private constructor(itemView: View):RecyclerView.ViewHolder(itemView) {

    private lateinit var item: TextView

    init {  item = itemView.findViewById(R.id.text)}

    fun bind(text: String?) {
        item.text = text
    }

    companion object {
        fun create(parent: ViewGroup): AgendaViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item, parent, false)
            return AgendaViewHolder(view)
        }
    }
}