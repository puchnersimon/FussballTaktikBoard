package at.fhhgb.mc.pro_fuballtaktikboard

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import at.fhhgb.mc.pro_fuballtaktikboard.databinding.ActivityThirdFieldBinding

class ActivityThirdField : Activity() {

    lateinit var binding: ActivityThirdFieldBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdFieldBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBackToSecondField.setOnClickListener {
            val intentBackToSecondField: Intent = Intent(this, ActivitySecondField::class.java)
            startActivity(intentBackToSecondField)
        }

        binding.buttonFieldBack.setOnClickListener {
            val intentBackToMenu: Intent = Intent(this, MainActivity::class.java)
            startActivity(intentBackToMenu)
        }

    }
}