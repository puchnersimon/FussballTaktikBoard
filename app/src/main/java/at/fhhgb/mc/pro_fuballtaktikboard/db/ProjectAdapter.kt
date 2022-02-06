package at.fhhgb.mc.pro_fuballtaktikboard.db

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.fhhgb.mc.pro_fuballtaktikboard.R
import at.fhhgb.mc.pro_fuballtaktikboard.models.Project

class ProjectAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listOfProjects = mutableListOf<Project>()

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

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val project = listOfProjects[position]

        val projectHolder = holder as ViewHolder

        projectHolder.projectName.text = project.projectName


        projectHolder.delete.setOnClickListener {
            //TODO --> Delete project from db and recyclerview
            listOfProjects.removeAt(position)
            notifyDataSetChanged()
        }

        projectHolder.edit.setOnClickListener {
            //TODO --> edit projectname
        }

    }

    override fun getItemCount(): Int {
        return listOfProjects.size
    }
}
