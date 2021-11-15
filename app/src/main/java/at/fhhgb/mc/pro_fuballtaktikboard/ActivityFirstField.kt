package at.fhhgb.mc.pro_fuballtaktikboard

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import at.fhhgb.mc.pro_fuballtaktikboard.databinding.ActivityFirstFieldBinding
import at.fhhgb.mc.pro_fuballtaktikboard.databinding.ActivityMainBinding

class ActivityFirstField : Activity() {
    lateinit var binding: ActivityFirstFieldBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstFieldBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonFieldBack.setOnClickListener {
            val intentBackToMain: Intent = Intent(this, MainActivity::class.java)
            startActivity(intentBackToMain)
        }

    }
}