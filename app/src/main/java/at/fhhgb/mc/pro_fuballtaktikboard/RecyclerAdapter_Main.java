package at.fhhgb.mc.pro_fuballtaktikboard;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RecyclerAdapter_Main extends RecyclerView.Adapter<RecyclerAdapter_Main.ViewHolder> {
    Context context;
    ArrayList<String> project;

    RecyclerAdapter_Main(Context context, ArrayList<String> project) {
        this.context = context;
        this.project = project;
    }


    @NonNull
    @NotNull
    @Override
    public RecyclerAdapter_Main.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_main_recycler_view_cell, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerAdapter_Main.ViewHolder holder, int position) {
        holder.projectname.setText(project.get(position));

        //delete item on list with delete button
        holder.delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                project.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(context, ActivityFirstField.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return project.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView projectname;
        ImageButton delete;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            projectname = itemView.findViewById(R.id.textView_rv_cell_project);
            delete = itemView.findViewById(R.id.button_rv_cell_removeItem);
        }

    }
}
