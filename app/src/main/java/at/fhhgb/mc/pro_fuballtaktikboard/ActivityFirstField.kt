package at.fhhgb.mc.pro_fuballtaktikboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import at.fhhgb.mc.pro_fuballtaktikboard.databinding.ActivityFirstFieldBinding

class ActivityFirstField : AppCompatActivity() {
    lateinit var binding: ActivityFirstFieldBinding


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

        val fragment = DrawFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.setReorderingAllowed(true)
        ft.replace(R.id.fragmentContainer_first_field, fragment)
        ft.commit()
    }
}