package at.fhhgb.mc.pro_fuballtaktikboard.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import at.fhhgb.mc.pro_fuballtaktikboard.models.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val databaseName = "taktik_board_db"

@Database(entities = [Project::class], version = 1, exportSchema = false)
public abstract class TaktikBoardDatabase : RoomDatabase() {

    abstract fun projectDao(): ProjectDao

    private class TaktikBoardDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val projectDao = database.projectDao()
                    projectDao.deleteProjectTable()
                }
            }
        }
    }


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: TaktikBoardDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): TaktikBoardDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaktikBoardDatabase::class.java,
                    databaseName
                )
                    .addCallback(TaktikBoardDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}