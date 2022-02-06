package at.fhhgb.mc.pro_fuballtaktikboard.models

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "project")
data class Project(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    var projectName: String,
    var pathFirstField: String,
    var pathPenaltyArea: String,
    var pathFreeArea: String,
    var hasEdited: Boolean
) {}