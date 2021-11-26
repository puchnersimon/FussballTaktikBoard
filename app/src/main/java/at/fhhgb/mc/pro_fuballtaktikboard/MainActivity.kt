package at.fhhgb.mc.pro_fuballtaktikboard

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.fhhgb.mc.pro_fuballtaktikboard.databinding.ActivityMainBinding
import at.fhhgb.mc.pro_fuballtaktikboard.databinding.CreateProjectBinding

class MainActivity : Activity() {

    lateinit var binding: ActivityMainBinding
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: RecyclerAdapter_Main
    var projectList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonMainAdd.setOnClickListener {
            /*
            //alert vor input - create new project
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Create new Project!")
            val input = EditText(this)
            input.setHint("new Projectname")
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)
            //set buttons for alert
            builder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
                projectList.add(input.text.toString())
            } )
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                dialog.cancel()
            })
            //show alert
            builder.show()

            //update RecyclerView_Main
            adapter.notifyDataSetChanged()

             */
            val view = View.inflate(this@MainActivity, R.layout.create_project, null)
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setView(view)

            val dialog = builder.create()
            dialog.show()

            dialog.findViewById<Button>(R.id.create_layout_confirm_button).setOnClickListener {
                var projectNameTF: EditText =
                    dialog.findViewById<EditText>(R.id.create_project_project_name)

                var projectName: String = projectNameTF.text.toString()
                projectList.add(projectName)
                dialog.cancel()
            }

            dialog.findViewById<Button>(R.id.create_layout_cancel_button).setOnClickListener {

                dialog.cancel()
            }

            //update RecyclerView_Main
            adapter.notifyDataSetChanged()

            //dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            //dialog.setCancelable(false)
        }
            recyclerView = findViewById(R.id.recycler_view_main)
            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter = RecyclerAdapter_Main(this, projectList)
            adapter.notifyDataSetChanged()
            recyclerView.adapter = adapter


            //testbutton
            binding.buttonTest.setOnClickListener {
                val intentNextAc: Intent = Intent(this, ActivityFirstField::class.java)
                startActivity(intentNextAc)
            }


        }


    }
