package at.fhhgb.mc.pro_fuballtaktikboard.db

import at.fhhgb.mc.pro_fuballtaktikboard.models.Project
import kotlinx.coroutines.flow.Flow

class ProjectRepository(private val projectDao: ProjectDao) {

    fun insert(project: Project) {
        projectDao.insert(project)
    }

    fun getProjects(): Flow<MutableList<Project>> {
        return projectDao.getAllProjects()
    }

    fun getProject(id: Int): Project {
        return projectDao.getProject(id)
    }

    fun update(project: Project) {
        projectDao.update(project)
    }

    fun delete(project: Project) {
        projectDao.delete(project)
    }

    fun deleteProjectTable() {
        projectDao.deleteProjectTable()
    }
}