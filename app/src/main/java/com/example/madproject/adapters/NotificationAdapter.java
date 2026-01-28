package com.example.madproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.R;
import com.example.madproject.models.Notification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context context;
    private List<Notification> notificationList;
    private OnNotificationClickListener listener;

    public interface OnNotificationClickListener {
        void onNotificationClick(Notification notification);
    }

    public NotificationAdapter(Context context, List<Notification> notificationList, OnNotificationClickListener listener) {
        this.context = context;
        this.notificationList = notificationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        // Set notification title
        holder.tvNotificationTitle.setText(notification.getTitle());

        // Set notification message
        holder.tvNotificationMessage.setText(notification.getMessage());

        // Set time ago
        holder.tvTime.setText(getTimeAgo(notification.getTimestamp()));

        // Set icon based on notification type
        setNotificationIcon(holder.ivNotificationIcon, notification.getType());

        // Show/hide unread indicator
        holder.unreadIndicator.setVisibility(notification.isRead() ? View.GONE : View.VISIBLE);

        // Set background based on read status
        if (!notification.isRead()) {
            holder.itemView.setBackgroundColor(0xFFF5F5F5); // Light grey for unread
        } else {
            holder.itemView.setBackgroundColor(0xFFFFFFFF); // White for read
        }

        // Hide action button for now
        holder.btnAction.setVisibility(View.GONE);

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onNotificationClick(notification);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    private void setNotificationIcon(ImageView imageView, String type) {
        int iconRes;
        switch (type.toLowerCase()) {
            case "job":
                iconRes = R.drawable.ic_jo;
                break;
            case "bid":
                iconRes = R.drawable.ic_bid;
                break;
            case "message":
                iconRes = R.drawable.ic_message;
                break;
            case "task":
                iconRes = R.drawable.ic_task;
                break;
            case "payment":
                iconRes = R.drawable.ic_payment;
                break;
            case "system":
            default:
                iconRes = R.drawable.ic_notifications;
                break;
        }
        imageView.setImageResource(iconRes);
    }

    private String getTimeAgo(long timestamp) {
        long now = System.currentTimeMillis();
        long diff = now - timestamp;

        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long weeks = days / 7;

        if (weeks > 0) {
            return weeks + "w ago";
        } else if (days > 0) {
            return days + "d ago";
        } else if (hours > 0) {
            return hours + "h ago";
        } else if (minutes > 0) {
            return minutes + "m ago";
        } else {
            return "Just now";
        }
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        ImageView ivNotificationIcon;
        View unreadIndicator;
        TextView tvNotificationTitle, tvTime, tvNotificationMessage;
        Button btnAction;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            ivNotificationIcon = itemView.findViewById(R.id.ivNotificationIcon);
            unreadIndicator = itemView.findViewById(R.id.unreadIndicator);
            tvNotificationTitle = itemView.findViewById(R.id.tvNotificationTitle);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvNotificationMessage = itemView.findViewById(R.id.tvNotificationMessage);
            btnAction = itemView.findViewById(R.id.btnAction);
        }
    }
}
