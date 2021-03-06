package at.fhhgb.mc.pro_fuballtaktikboard.db

import androidx.room.*
import at.fhhgb.mc.pro_fuballtaktikboard.models.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("select * from project order by id asc")
    fun getAllProjects(): Flow<MutableList<Project>>

    @Query("select * from project where id = :id")
    fun getProject(id: Int): Project

    @Insert
    fun insert(project: Project)

    @Update
    fun update(project: Project)

    @Delete
    fun delete(project: Project)

    @Query("delete from project")
    fun deleteProjectTable()
}