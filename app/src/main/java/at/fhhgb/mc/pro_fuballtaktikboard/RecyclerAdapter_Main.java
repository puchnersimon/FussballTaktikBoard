package at.fhhgb.mc.pro_fuballtaktikboard;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RecyclerAdapter_Main extends RecyclerView.Adapter<RecyclerAdapter_Main.ViewHolder2> {
    Context context;
    ArrayList<String> project;

    RecyclerAdapter_Main(Context context, ArrayList<String> project) {
        this.context = context;
        this.project = project;
    }


    @NonNull
    @NotNull
    @Override
    public ViewHolder2 onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_main_recycler_view_cell, parent, false);
        ViewHolder2 viewHolder2 = new ViewHolder2(view);
        return viewHolder2;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder2 holder, int position) {
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

    public class ViewHolder2 extends RecyclerView.ViewHolder {
        View itemView;
        TextView projectname;
        ImageButton delete;

        public ViewHolder2(@NonNull @NotNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            projectname = itemView.findViewById(R.id.textView_rv_cell_project);
            delete = itemView.findViewById(R.id.button_rv_cell_removeItem);
        }

    }
}
