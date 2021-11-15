package at.fhhgb.mc.pro_fuballtaktikboard

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.fhhgb.mc.pro_fuballtaktikboard.databinding.ActivityMainBinding

class MainActivity : Activity() {

    lateinit var binding: ActivityMainBinding
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: RecyclerAdapter_Main
    var ProjectList: ArrayList<String> = ArrayList()

    // test

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonMainAdd.setOnClickListener {
            //alert vor input - create new project
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Create new Project!")
            val input = EditText(this)
            input.setHint("new Projectname")
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)
            //set buttons for alert
            builder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
                ProjectList.add(input.text.toString())
            } )
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                dialog.cancel()
            })
            //show alert
            builder.show()

            //update RecyclerView_Main
            adapter.notifyDataSetChanged()
        }

        recyclerView = findViewById(R.id.recycler_view_main)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecyclerAdapter_Main(this, ProjectList)
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter


        //testbutton - went to next action
        binding.buttonTest.setOnClickListener {
            val intentNextAc: Intent = Intent(this, ActivityFirstField::class.java)
            startActivity(intentNextAc)
        }


    }
}