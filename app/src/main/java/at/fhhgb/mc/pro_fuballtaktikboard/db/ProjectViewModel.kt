package at.fhhgb.mc.pro_fuballtaktikboard.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import at.fhhgb.mc.pro_fuballtaktikboard.models.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ProjectViewModel(private val repository: ProjectRepository) : ViewModel() {

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(project: Project) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(project)
    }

    /**
     * returns a LivaData list of the tiles
     */
    fun getProjects(): Flow<List<Project>> {
        return repository.getProjects()
    }
}

class ProjectViewModelFactory(private val repository: ProjectRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProjectViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProjectViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}