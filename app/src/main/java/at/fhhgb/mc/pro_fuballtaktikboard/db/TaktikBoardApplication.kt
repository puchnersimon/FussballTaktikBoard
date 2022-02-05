package at.fhhgb.mc.pro_fuballtaktikboard.db

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class TaktikBoardApplication: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { TaktikBoardDatabase.getDatabase(this, applicationScope) }

    val projectRepository by lazy { ProjectRepository(database.projectDao()) }
}