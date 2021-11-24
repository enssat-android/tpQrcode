package fr.enssat.kikekou.viewmodels

import androidx.lifecycle.*
import fr.enssat.kikekou.room.Agenda
import fr.enssat.kikekou.room.AgendaRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class AgendaViewModel (private val repository: AgendaRepository) : ViewModel() {

    // update UI when agendas datachanges.
    // Repository separated from UI through ViewModel.
    val allAgendas = repository.allAgendas.asLiveData()

    private val scope = CoroutineScope(SupervisorJob())

    /**
     * insert the data in non-blocking coroutine
     */
    fun insertAgenda(agd: Agenda) = scope.launch {
        repository.insert(agd)
    }
}
