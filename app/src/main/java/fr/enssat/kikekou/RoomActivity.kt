package fr.enssat.kikekou

import android.widget.Toast

import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import fr.enssat.kikekou.adapters.AgendaListAdapter
import fr.enssat.kikekou.json.AgendaJsonParser
import fr.enssat.kikekou.room.*
import fr.enssat.kikekou.viewmodels.AgendaViewModel
import fr.enssat.kikekou.viewmodels.AgendaViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


class RoomActivity: AppCompatActivity() {
    private lateinit var _agendaViewModel: AgendaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        val scope = CoroutineScope(SupervisorJob())
        val database = AgendaRoomDatabase.getDatabase(this,scope )
        val repository  =  AgendaRepository(database.agendaDao())

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = AgendaListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Get a new or existing ViewModel using Agenda view model factory.
        _agendaViewModel = ViewModelProvider(this, AgendaViewModelFactory(repository)).get(AgendaViewModel::class.java)
        _agendaViewModel.allAgendas.observe(this) { agendas ->
            adapter.submitList(agendas)
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view: View? ->
            //val contact = Contact("gil.lebrun@orange.com",null, null)
            //val location = Location(4,"a l \'enssat")
            //val agenda = Agenda("gilles le brun", 47, location,contact)

            var json =""
            val agenda = AgendaJsonParser.parseAgenda(json)
            agenda?.let{
                _agendaViewModel.insertAgenda(it)
            }

        }

        var json =""


    }
}