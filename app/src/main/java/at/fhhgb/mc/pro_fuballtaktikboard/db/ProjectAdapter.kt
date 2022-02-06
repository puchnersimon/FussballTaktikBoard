package at.fhhgb.mc.pro_fuballtaktikboard.db

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.fhhgb.mc.pro_fuballtaktikboard.ActivityFirstField
import at.fhhgb.mc.pro_fuballtaktikboard.R
import at.fhhgb.mc.pro_fuballtaktikboard.models.Project
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProjectAdapter(projectViewModel: ProjectViewModel) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var projectViewModel = projectViewModel
    private var listOfProjects = mutableListOf<Project>()
    private lateinit var context : Context

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var projectName: TextView = view.findViewById(R.id.textView_rv_cell_project)
        var delete: ImageButton = view.findViewById(R.id.button_rv_cell_removeItem)
        var edit: ImageButton = view.findViewById(R.id.button_rv_cell_edit_projectname)
    }

    fun setProjects(listOfProjects: MutableList<Project>) {
        this.listOfProjects = listOfProjects
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_main_recycler_view_cell, parent, false)

        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val project = listOfProjects[position]

        val projectHolder = holder as ViewHolder

        projectHolder.projectName.text = project.projectName


        // delete
        projectHolder.delete.setOnClickListener {
            GlobalScope.launch {
                projectViewModel.delete(project)
            }
        }

        // edit
        projectHolder.edit.setOnClickListener {
            //TODO --> edit projectname
        }

        // edit project
        projectHolder.itemView.setOnClickListener {
            context.let {
                val intent = Intent(it, ActivityFirstField::class.java)
                intent.putExtra("PROJECT_ID", project.id)
                context.startActivity(intent)
            }
        }

    }

    override fun getItemCount(): Int {
        return listOfProjects.size
    }
}
