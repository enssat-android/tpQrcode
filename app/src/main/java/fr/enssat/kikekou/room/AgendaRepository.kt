package fr.enssat.kikekou.room

import androidx.annotation.WorkerThread

class AgendaRepository(private val dao: AgendaDao) {
    val allAgendas = dao.getAlphabetizedAgendas()

    // call on a non-UI thread
    @WorkerThread
    suspend fun insert(agenda: Agenda) {
        dao.insert(agenda)
    }
}

