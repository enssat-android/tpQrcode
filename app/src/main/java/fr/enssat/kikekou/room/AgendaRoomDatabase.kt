package fr.enssat.kikekou.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [Agenda::class], version = 1)
abstract class AgendaRoomDatabase : RoomDatabase() {

        abstract fun agendaDao(): AgendaDao

        companion object {
            @Volatile
            private var INSTANCE: AgendaRoomDatabase? = null

            fun getDatabase(
                context: Context,
                scope: CoroutineScope
            ): AgendaRoomDatabase {
                // if the INSTANCE is not null, then return it,
                // if it is, then create the database
                return INSTANCE ?: synchronized(this) {
                    val aux = Room.databaseBuilder(
                        context.applicationContext,
                        AgendaRoomDatabase::class.java,
                        "agenda_database"
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(AgendaDatabaseCallback(scope))
                        .build()

                    INSTANCE = aux
                    aux
                }
            }

            private class AgendaDatabaseCallback(val scope: CoroutineScope) : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    INSTANCE?.let { database ->
                        scope.launch(Dispatchers.IO) {
                            populate(database.agendaDao())
                        }
                    }
                }
            }

            /* populate db in coroutine*/
            suspend fun populate(agendaDao: AgendaDao) {
                agendaDao.deleteAll()
               var contact = Contact("guillaume.chatelet@orange.com", null, null)
                var location = Location(2, "en TT")
                var agenda = Agenda("guillaume chatelet", 47, location,contact)
                agendaDao.insert(agenda)

                contact = Contact("pierre.crepieux@orange.com", null, null)
                location = Location(3, "en TT")
                agenda = Agenda("pierre crepieux",  47, location,contact)
                agendaDao.insert(agenda)
             }
        }
}
