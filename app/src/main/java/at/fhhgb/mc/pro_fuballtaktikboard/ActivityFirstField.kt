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
import android.widget.Toast
import at.fhhgb.mc.pro_fuballtaktikboard.databinding.ActivityFirstFieldBinding
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ActivityFirstField : AppCompatActivity() {
    lateinit var binding: ActivityFirstFieldBinding
    private lateinit var fragment: DrawFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstFieldBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonNextField.setOnClickListener {
            val intentToNextField: Intent = Intent(this, ActivitySecondField::class.java)
            startActivity(intentToNextField)
        }

        binding.buttonFieldBack.setOnClickListener {
            val intentBackToMain: Intent = Intent(this, MainActivity::class.java)
            startActivity(intentBackToMain)
        }

        fragment = DrawFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.setReorderingAllowed(true)
        ft.replace(R.id.fragmentContainer_first_field, fragment)
        ft.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        saveField()
    }

    /**
     * save the bitmap to app folder and stores it in room db
     */
    private fun saveField() {
        val bitmap = fragment.getBitmap()
        saveMediaToStorage(bitmap)
    }


    fun saveMediaToStorage(bitmap: Bitmap) {
        //Generating a file name
        val filename = "${System.currentTimeMillis()}.jpg"

        //Output stream
        var fos: OutputStream? = null

        //For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
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

                //Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            //These for devices running on android < Q
            //So I don't think an explanation is needed here
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            //Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(applicationContext, "save", Toast.LENGTH_SHORT)
        }
    }
}