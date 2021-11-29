package fr.enssat.kikekou.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.enssat.kikekou.room.AgendaRepository

class AgendaViewModelFactory(private val repository: AgendaRepository): ViewModelProvider.Factory {
   @Suppress("UNCHECKED_CAST")
    //factory created to pass repository to view model...
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AgendaViewModel(repository) as T
    }
 }
