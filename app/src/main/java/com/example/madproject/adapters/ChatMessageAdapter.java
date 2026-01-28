package com.example.madproject.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.R;
import com.example.madproject.models.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * ChatMessageAdapter
 *
 * Displays chat messages in a RecyclerView with different styles for:
 * - User messages (right side, purple background)
 * - AI messages (left side, white background)
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MessageViewHolder> {

    private Context context;
    private List<ChatMessage> messageList;

    public ChatMessageAdapter(Context context, List<ChatMessage> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);

        // Set message text
        holder.tvMessage.setText(message.getMessage());

        // Set timestamp
        String time = formatTime(message.getTimestamp());
        holder.tvTime.setText(time);

        // Style based on sender
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.messageCard.getLayoutParams();

        if (message.isUser()) {
            // User message (right side, purple background)
            params.gravity = Gravity.END;
            params.setMargins(48, 8, 8, 8);
            holder.messageCard.setLayoutParams(params);

            holder.messageCard.setCardBackgroundColor(0xFF7C4DFF); // Purple
            holder.tvMessage.setTextColor(0xFFFFFFFF); // White text
            holder.tvTime.setTextColor(0xFFE1BEE7); // Light purple
        } else {
            // AI message (left side, white background)
            params.gravity = Gravity.START;
            params.setMargins(8, 8, 48, 8);
            holder.messageCard.setLayoutParams(params);

            holder.messageCard.setCardBackgroundColor(0xFFFFFFFF); // White
            holder.tvMessage.setTextColor(0xFF212121); // Dark text
            holder.tvTime.setTextColor(0xFF9E9E9E); // Grey
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    /**
     * Format timestamp to readable time
     */
    private String formatTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        CardView messageCard;
        TextView tvMessage, tvTime;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageCard = itemView.findViewById(R.id.messageCard);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}