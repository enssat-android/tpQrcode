package fr.enssat.kikekou

import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import fr.enssat.kikekou.adapters.AgendaListAdapter
import fr.enssat.kikekou.databinding.ActivityRoomBinding
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

        val binding = ActivityRoomBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())

        val adapter = AgendaListAdapter()
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        val scope = CoroutineScope(SupervisorJob())
        val database = AgendaRoomDatabase.getDatabase(this, scope)
        val repository = AgendaRepository(database.agendaDao())

        // Get a new or existing ViewModel using Agenda view model factory.
        _agendaViewModel = ViewModelProvider(
            this,
            AgendaViewModelFactory(repository)
        ).get(AgendaViewModel::class.java)
        _agendaViewModel.allAgendas.observe(this) { agendas ->
            adapter.submitList(agendas)
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view: View? ->
             var json =
                "{\"name\":\"gilles le brun\",\"contacts\":{ \"mail\": \"gil.lebrun@orange.com\"},\"week\": 43,\"loc\":[{\"day\": 1, \"place\": \"teletravail\"},{\"day\": 2, \"place\": \"Bureau WF024\"}]}"
            val agenda = AgendaJsonParser.parseAgenda(json)
            agenda?.let {
                _agendaViewModel.insertAgenda(it)
            }
        }
    }
}