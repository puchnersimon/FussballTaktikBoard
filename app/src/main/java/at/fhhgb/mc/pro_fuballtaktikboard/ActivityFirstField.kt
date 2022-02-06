package at.fhhgb.mc.pro_fuballtaktikboard

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import at.fhhgb.mc.pro_fuballtaktikboard.databinding.ActivityFirstFieldBinding
import at.fhhgb.mc.pro_fuballtaktikboard.db.ProjectViewModel
import at.fhhgb.mc.pro_fuballtaktikboard.db.ProjectViewModelFactory
import at.fhhgb.mc.pro_fuballtaktikboard.db.TaktikBoardApplication
import at.fhhgb.mc.pro_fuballtaktikboard.models.Project
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import android.graphics.BitmapFactory
import java.io.InputStream


class ActivityFirstField : AppCompatActivity() {
    lateinit var binding: ActivityFirstFieldBinding
    private lateinit var fragment: DrawFragment
    private lateinit var project: Project
    private lateinit var projectViewModelFactory: ProjectViewModelFactory
    private lateinit var projectViewModel: ProjectViewModel
    private var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstFieldBinding.inflate(layoutInflater)
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
                    if (!project.pathFirstField.isNullOrEmpty()) {
                        //Output stream
                        var fos: InputStream? = null

                        //For devices running android >= Q
                        //getting the contentResolver
                        applicationContext?.contentResolver?.also { resolver ->

                            //Inserting the contentValues to contentResolver and getting the Uri
                            val imageUri: Uri = Uri.parse(project.pathFirstField)


                            //Opening an outputstream with the Uri that we got
                            fos = imageUri.let { resolver.openInputStream(it) }
                        }

                        fos?.use {
                            bitmap = BitmapFactory.decodeStream(it);
                        }
                    }

                    fragment = DrawFragment(projectViewModel, project, FIRST_FIELD, bitmap)
                    val ft = supportFragmentManager.beginTransaction()
                    ft.setReorderingAllowed(true)
                    ft.replace(R.id.fragmentContainer_first_field, fragment)
                    ft.commit()
                }
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

        binding.buttonNextField.setOnClickListener {
            saveMediaToStorage()
            val intentToNextField: Intent = Intent(this, ActivitySecondField::class.java)
            intentToNextField.putExtra("PROJECT_ID", project.id)
            startActivity(intentToNextField)
        }

        binding.buttonFieldBack.setOnClickListener {
            saveMediaToStorage()
            val intentBackToMain: Intent = Intent(this, MainActivity::class.java)
            intentBackToMain.putExtra("PROJECT_ID", project.id)
            startActivity(intentBackToMain)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        saveMediaToStorage()
    }

    private fun saveMediaToStorage() {
        val bitmap = fragment.getBitmap()
        //Generating a file name
        val filename = "${project.projectName + "_" + FIRST_FIELD}.jpg"

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
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
            }
            //Inserting the contentValues to contentResolver and getting the Uri
            val imageUri: Uri? =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues, )

            project.let {
                it.pathFirstField = imageUri.toString()
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