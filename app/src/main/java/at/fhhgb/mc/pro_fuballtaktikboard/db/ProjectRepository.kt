package at.fhhgb.mc.pro_fuballtaktikboard.db

import at.fhhgb.mc.pro_fuballtaktikboard.models.Project
import kotlinx.coroutines.flow.Flow

class ProjectRepository(private val projectDao: ProjectDao) {

    fun insert(project: Project) {
        projectDao.insert(project)
    }

    fun getProjects(): Flow<List<Project>> {
        return projectDao.getAllProjects()
    }

    fun delete(project: Project) {
        projectDao.delete(project)
    }
}