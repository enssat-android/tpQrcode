package fr.enssat.kikekou.adapters

import androidx.recyclerview.widget.RecyclerView
import fr.enssat.kikekou.databinding.RecyclerviewItemBinding


class AgendaViewHolder private constructor(var binding:RecyclerviewItemBinding):RecyclerView.ViewHolder(binding.root) {

    fun bind(value: String?) {
        binding.agendaItem.text = value
    }

    companion object {
        fun create(binding: RecyclerviewItemBinding): AgendaViewHolder {
            return AgendaViewHolder(binding)
        }
    }
}