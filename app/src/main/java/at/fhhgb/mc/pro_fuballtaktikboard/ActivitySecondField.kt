package at.fhhgb.mc.pro_fuballtaktikboard

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import at.fhhgb.mc.pro_fuballtaktikboard.databinding.ActivitySecondFieldBinding
import at.fhhgb.mc.pro_fuballtaktikboard.db.ProjectViewModel
import at.fhhgb.mc.pro_fuballtaktikboard.db.ProjectViewModelFactory
import at.fhhgb.mc.pro_fuballtaktikboard.db.TaktikBoardApplication
import at.fhhgb.mc.pro_fuballtaktikboard.models.Project
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.InputStream
import java.io.OutputStream

class ActivitySecondField : AppCompatActivity() {

    lateinit var binding: ActivitySecondFieldBinding
    private lateinit var project: Project
    private lateinit var projectViewModelFactory: ProjectViewModelFactory
    private lateinit var projectViewModel: ProjectViewModel
    private var bitmap: Bitmap? = null
    private lateinit var fragment: DrawFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondFieldBinding.inflate(layoutInflater)
        setContentView(binding.root)

        projectViewModelFactory = ProjectViewModelFactory((application as TaktikBoardApplication).projectRepository)
        projectViewModel = ViewModelProvider(this, projectViewModelFactory)[ProjectViewModel::class.java]

        try {
            val projectId = intent.getIntExtra("PROJECT_ID", 0)

            GlobalScope.launch {
                project = projectViewModel.getProject(projectId)

                // start fragment when the project is received
                runBlocking {

                    // load jpg from gallery to object
                    if (!project.pathPenaltyArea.isNullOrEmpty()) {
                        //Output stream
                        var fos: InputStream? = null

                        //For devices running android >= Q
                        //getting the contentResolver
                        applicationContext?.contentResolver?.also { resolver ->

                            //Inserting the contentValues to contentResolver and getting the Uri
                            val imageUri: Uri = Uri.parse(project.pathPenaltyArea)


                            //Opening an outputstream with the Uri that we got
                            fos = imageUri.let { resolver.openInputStream(it) }
                        }

                        fos?.use {
                            bitmap = BitmapFactory.decodeStream(it);
                        }
                    }

                    fragment = DrawFragment(projectViewModel, project, SECOND_FIELD, bitmap)
                    val ft = supportFragmentManager.beginTransaction()
                    ft.setReorderingAllowed(true)
                    ft.replace(R.id.fragmentContainer_second_field, fragment)
                    ft.commit()
                }
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

        binding.buttonBackToFirstField.setOnClickListener {
            saveMediaToStorage()
            val intentBackToFirstField: Intent = Intent(this, ActivityFirstField::class.java)
            intentBackToFirstField.putExtra("PROJECT_ID", project.id)
            startActivity(intentBackToFirstField)
        }

        binding.buttonNextField.setOnClickListener {
            saveMediaToStorage()
            val intentToNextField: Intent = Intent(this, ActivityThirdField::class.java)
            intentToNextField.putExtra("PROJECT_ID", project.id)
            startActivity(intentToNextField)
        }

        binding.buttonFieldBack.setOnClickListener {
            saveMediaToStorage()
            val intentBackToMenu: Intent = Intent(this, MainActivity::class.java)
            startActivity(intentBackToMenu)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        saveMediaToStorage()
    }

    private fun saveMediaToStorage() {
        val bitmap = fragment.getBitmap()
        //Generating a file name
        val filename = "${project.projectName + "_" + SECOND_FIELD}.jpg"

        //Output stream
        var fos: OutputStream? = null

        //For devices running android >= Q
        //getting the contentResolver
        applicationContext?.contentResolver?.also { resolver ->

            //Content resolver will process the contentvalues
            val contentValues = ContentValues().apply {

                //putting file information in content values
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }

            //Inserting the contentValues to contentResolver and getting the Uri
            val imageUri: Uri? =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            project.let {
                it.pathPenaltyArea = imageUri.toString()
                it.hasEdited = true

                GlobalScope.launch {
                    projectViewModel.update(it)
                }
            }

            //Opening an outputstream with the Uri that we got
            fos = imageUri?.let { resolver.openOutputStream(it) }
        }

        fos?.use {
            //Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
    }
}