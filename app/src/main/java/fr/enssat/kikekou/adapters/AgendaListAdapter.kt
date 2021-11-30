package fr.enssat.kikekou.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import fr.enssat.kikekou.databinding.RecyclerviewItemBinding
import fr.enssat.kikekou.room.Agenda

class AgendaListAdapter(): ListAdapter<Agenda, AgendaViewHolder>(AgendaDiff()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgendaViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            var binding = RecyclerviewItemBinding.inflate(inflater, parent, false)
            return AgendaViewHolder.create(binding)
        }

        override fun onBindViewHolder(holder: AgendaViewHolder, position: Int) {
            val current: Agenda? = getItem(position)
            holder.bind(current?.name + " / " + current?.contact?.mail)
        }

        internal class AgendaDiff : DiffUtil.ItemCallback<Agenda>() {
            override fun areItemsTheSame(oldItem: Agenda, newItem: Agenda): Boolean {
                return oldItem === newItem
            }
            override fun areContentsTheSame(oldItem: Agenda, newItem: Agenda): Boolean {
                return oldItem.name.equals(newItem.name)
            }
        }
}