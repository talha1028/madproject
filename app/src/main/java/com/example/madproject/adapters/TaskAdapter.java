package com.example.madproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.R;
import com.example.madproject.models.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private List<Task> taskList;
    private OnTaskClickListener listener;

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
    }

    public TaskAdapter(Context context, List<Task> taskList, OnTaskClickListener listener) {
        this.context = context;
        this.taskList = taskList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task_card, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        // Set task title
        holder.tvTaskTitle.setText(task.getTaskTitle());

        // Set worker count
        holder.tvTaskNumber.setText(String.valueOf(task.getNumberOfWorkers()));

        // Set last updated
        String lastUpdatedText = "Last update: " + formatDate(task.getUpdatedAt());
        holder.tvLastUpdated.setText(lastUpdatedText);

        // Set wages
        String wagesText = "Wages: Rs. " + formatCurrency(task.getDailyWages()) + " /day";
        holder.tvWages.setText(wagesText);

        // Set progress
        String progressText = task.getCompletedQuantity() + "/" + task.getEstimatedQuantity() + " " + task.getProgressUnit();
        holder.tvProgress.setText(progressText);

        // Set date range
        String dateRange = "";
        if (task.getStartDate() != 0 && task.getEndDate() != 0) {
            dateRange = formatDate(task.getStartDate()) + " - " + formatDate(task.getEndDate());
        } else if (task.getStartDate() != 0) {
            dateRange = "Started: " + formatDate(task.getStartDate());
        } else {
            dateRange = "Not started";
        }
        holder.tvDateRange.setText(dateRange);

        // Set status with color
        String statusText = task.getStatus().replace("_", " ");
        holder.tvStatus.setText(statusText);
        setStatusColor(holder.tvStatus, task.getStatus());

        // Set assigned by info
        holder.tvAssignedBy.setText(task.getCreatedBy() != null ? task.getCreatedBy() : "Admin");

        // Set assigned to info
        String assignedTo = task.getAssignedTo() != null && !task.getAssignedTo().equals("TBD")
                          ? task.getAssignedTo()
                          : "Unassigned";
        holder.tvAssignedTo.setText(assignedTo);

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTaskClick(task);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    private String formatCurrency(double amount) {
        if (amount >= 10000000) { // 1 Crore
            return String.format("%.1f Cr", amount / 10000000);
        } else if (amount >= 100000) { // 1 Lakh
            return String.format("%.1f L", amount / 100000);
        } else if (amount >= 1000) { // 1 Thousand
            return String.format("%.1f K", amount / 1000);
        } else {
            return String.format("%.0f", amount);
        }
    }

    private String formatDate(long timestamp) {
        if (timestamp == 0) {
            return "N/A";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM ''yy", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    private void setStatusColor(TextView textView, String status) {
        int color;
        switch (status.toLowerCase()) {
            case "not_started":
                color = 0xFF757575; // Grey
                break;
            case "ongoing":
                color = 0xFFFFA726; // Orange
                break;
            case "completed":
                color = 0xFF4CAF50; // Green
                break;
            default:
                color = 0xFF757575; // Grey
                break;
        }
        textView.setTextColor(color);
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTaskTitle, tvTaskNumber, tvLastUpdated, tvWages, tvProgress,
                 tvDateRange, tvStatus, tvAssignedBy, tvAssignedTo;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvTaskNumber = itemView.findViewById(R.id.tvTaskNumber);
            tvLastUpdated = itemView.findViewById(R.id.tvLastUpdated);
            tvWages = itemView.findViewById(R.id.tvWages);
            tvProgress = itemView.findViewById(R.id.tvProgress);
            tvDateRange = itemView.findViewById(R.id.tvDateRange);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvAssignedBy = itemView.findViewById(R.id.tvAssignedBy);
            tvAssignedTo = itemView.findViewById(R.id.tvAssignedTo);
        }
    }
}
