package at.fhhgb.mc.pro_fuballtaktikboard

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.fhhgb.mc.pro_fuballtaktikboard.databinding.ActivityMainBinding
import at.fhhgb.mc.pro_fuballtaktikboard.db.ProjectAdapter
import at.fhhgb.mc.pro_fuballtaktikboard.db.ProjectViewModel
import at.fhhgb.mc.pro_fuballtaktikboard.db.ProjectViewModelFactory
import at.fhhgb.mc.pro_fuballtaktikboard.db.TaktikBoardApplication
import at.fhhgb.mc.pro_fuballtaktikboard.models.Project
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.coroutineContext


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: RecyclerAdapter_Main
    var projectList: ArrayList<String> = ArrayList()
    private lateinit var projectViewModelFactory: ProjectViewModelFactory
    private lateinit var projectViewModel: ProjectViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        projectViewModelFactory = ProjectViewModelFactory((application as TaktikBoardApplication).projectRepository)

        val projectAdapter = ProjectAdapter()
        with(binding.recyclerViewMain) {
            setHasFixedSize(true)
            adapter = projectAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        projectViewModel = ViewModelProvider(this, projectViewModelFactory)[ProjectViewModel::class.java]

        lifecycleScope.launchWhenStarted {
            projectViewModel.getProjects().collect {
                projectAdapter.setProjects(it)
            }
        }

        binding.buttonMainAdd.setOnClickListener {
            val view = View.inflate(this@MainActivity, R.layout.create_project, null)
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setView(view)

            val dialog = builder.create()
            dialog.show()

            dialog.findViewById<Button>(R.id.create_layout_confirm_button).setOnClickListener {

                var projectNameTF: EditText =
                    dialog.findViewById<EditText>(R.id.create_project_project_name)

                    var projectName: String = projectNameTF.text.toString()

                if (projectName != "") {
                    projectList.add(projectName)

                    val project = Project(
                        id = null,
                        projectName = projectName,
                        pathFirstField = "",
                        pathPenaltyArea = "",
                        pathFreeArea = ""
                    )

                    projectViewModel.insert(project)

                    dialog.cancel()
                } else {
                    Toast.makeText(this, "Set a Projectname!", Toast.LENGTH_LONG).show()
                }
            }

            dialog.findViewById<Button>(R.id.create_layout_cancel_button).setOnClickListener {
                dialog.cancel()
            }

            //update RecyclerView_Main
            //adapter.notifyDataSetChanged()

            //dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            //dialog.setCancelable(false)
        }

        binding.buttonMainDeleteAllProjects.setOnClickListener {
            //TODO --> remove all projects from list
            projectViewModel.deleteProjectTable()
            projectList.clear()
            Toast.makeText(this, "All projects deleted!", Toast.LENGTH_SHORT).show()
        }


        binding.buttonTest.setOnClickListener {
            val intentNextAc: Intent = Intent(this, ActivityFirstField::class.java)
            startActivity(intentNextAc)
        }
        /*

        recyclerView = findViewById(R.id.recycler_view_main)
            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter = RecyclerAdapter_Main(this, projectList)
            adapter.notifyDataSetChanged()
            recyclerView.adapter = adapter


            //testbutton


*/
        }

    }
