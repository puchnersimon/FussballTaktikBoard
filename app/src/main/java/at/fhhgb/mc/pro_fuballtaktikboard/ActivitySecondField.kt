package at.fhhgb.mc.pro_fuballtaktikboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import at.fhhgb.mc.pro_fuballtaktikboard.databinding.ActivitySecondFieldBinding

class ActivitySecondField : AppCompatActivity() {

    lateinit var binding: ActivitySecondFieldBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondFieldBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBackToFirstField.setOnClickListener {
            val intentBackToFirstField: Intent = Intent(this, ActivityFirstField::class.java)
            startActivity(intentBackToFirstField)
        }

        binding.buttonNextField.setOnClickListener {
            val intentToNextField: Intent = Intent(this, ActivityThirdField::class.java)
            startActivity(intentToNextField)
        }

        binding.buttonFieldBack.setOnClickListener {
            val intentBackToMenu: Intent = Intent(this, MainActivity::class.java)
            startActivity(intentBackToMenu)
        }

        val fragment = DrawFragment2()
        val ft = supportFragmentManager.beginTransaction()
        ft.setReorderingAllowed(true)
        ft.replace(R.id.fragmentContainer_second_field, fragment)
        ft.commit()

    }
}