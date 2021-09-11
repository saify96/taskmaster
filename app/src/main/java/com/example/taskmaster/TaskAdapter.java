package com.example.taskmaster;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    List<com.amplifyframework.datastore.generated.model.Task> tasksList = new ArrayList<>();

    public TaskAdapter(List<com.amplifyframework.datastore.generated.model.Task> tasksList) {
        this.tasksList = tasksList;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        public com.amplifyframework.datastore.generated.model.Task task;
        View itemView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;

            itemView.setOnClickListener(view -> {
                Intent goToDetailPage = new Intent(view.getContext(),TaskDetail.class);
                goToDetailPage.putExtra("title",task.getTitle());
                goToDetailPage.putExtra("description",task.getDescription());
                goToDetailPage.putExtra("state",task.getStatus());
                view.getContext().startActivity(goToDetailPage);
            });
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task, parent, false);
        TaskViewHolder taskViewHolder = new TaskViewHolder(view);
        return taskViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.task = tasksList.get(position);
        TextView taskTitle = holder.itemView.findViewById(R.id.titleFragment);
        TextView taskDescription = holder.itemView.findViewById(R.id.descFragment);
        TextView taskState = holder.itemView.findViewById(R.id.stateFragment);
        TextView teamName = holder.itemView.findViewById(R.id.teamFreagment);
        taskTitle.setText(holder.task.getTitle());
        taskDescription.setText(holder.task.getDescription());
        taskState.setText("State: " + holder.task.getStatus());
        teamName.setText("Team: "+ holder.task.getTeam().getName());
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }
}
