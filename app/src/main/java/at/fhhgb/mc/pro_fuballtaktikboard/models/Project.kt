package at.fhhgb.mc.pro_fuballtaktikboard.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "project")
data class Project(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    var projectName: String,
    var pathFirstField: String,
    var pathPenaltyArea: String,
    var pathFreeArea: String
) {}