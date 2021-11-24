package fr.enssat.kikekou.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AgendaDao {
    // allowing the insert of agenda with conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(agenda: Agenda)

    @Query("DELETE FROM agenda_table")
    fun deleteAll()

    @Query("SELECT * FROM agenda_table ORDER BY name ASC")
    fun getAlphabetizedAgendas(): Flow<List<Agenda>>
}

