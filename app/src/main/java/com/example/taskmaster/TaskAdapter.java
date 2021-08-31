package com.example.taskmaster;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    List<Task> tasksList = new ArrayList<Task>();

    public TaskAdapter(List<Task> tasksList) {
        this.tasksList = tasksList;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        public Task task;
        View itemView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
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
        taskTitle.setText(holder.task.title);
        taskDescription.setText(holder.task.description);
        taskState.setText("State: " + holder.task.state);
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }
}
