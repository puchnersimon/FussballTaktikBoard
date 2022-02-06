package at.fhhgb.mc.pro_fuballtaktikboard.db

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import at.fhhgb.mc.pro_fuballtaktikboard.R
import at.fhhgb.mc.pro_fuballtaktikboard.models.Project

class ProjectAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listOfProjects = emptyList<Project>()

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var projectName: TextView = view.findViewById(R.id.textView_rv_cell_project)
        var delete: ImageButton = view.findViewById(R.id.button_rv_cell_removeItem)
        var edit: ImageButton = view.findViewById(R.id.button_rv_cell_edit_projectname)
    }

    fun setProjects(listOfProjects: List<Project>) {
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


        //TODO --> Delete project from db and recyclerview
        //TODO --> edit projectname

        holder.delete.setOnClickListener {
            println("test delete")
        }
    }

    override fun getItemCount(): Int {
        return listOfProjects.size
    }
}
