package com.example.madproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.R;
import com.example.madproject.models.Material;

import java.util.List;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder> {

    private Context context;
    private List<Material> materialList;
    private OnMaterialClickListener listener;

    public interface OnMaterialClickListener {
        void onMaterialClick(Material material);
    }

    public MaterialAdapter(Context context, List<Material> materialList, OnMaterialClickListener listener) {
        this.context = context;
        this.materialList = materialList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_material_card, parent, false);
        return new MaterialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialViewHolder holder, int position) {
        Material material = materialList.get(position);

        // Set material name
        holder.tvMaterialName.setText(material.getMaterialName());

        // Set stock status with color
        holder.tvStockStatus.setText(material.getStatus().replace("_", " "));
        setStatusColor(holder.tvStockStatus, material.getStatus());

        // Set category and unit
        String categoryText = material.getCategory() + " • " + capitalizeFirst(material.getUnit());
        holder.tvCategory.setText(categoryText);

        // Set quantity
        holder.tvQuantity.setText(String.valueOf((int) material.getQuantity()));

        // Set price
        String priceText = "Rs. " + formatCurrency(material.getUnitPrice());
        holder.tvPrice.setText(priceText);

        // Set last updated
        String lastUpdatedText = "Updated: " + getRelativeTime(material.getLastUpdated());
        holder.tvLastUpdated.setText(lastUpdatedText);

        // Set supplier
        String supplierText = "Supplier: " + (material.getSupplier() != null ? material.getSupplier() : "N/A");
        holder.tvSupplier.setText(supplierText);

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMaterialClick(material);
            }
        });
    }

    @Override
    public int getItemCount() {
        return materialList.size();
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

    private String getRelativeTime(long timestamp) {
        if (timestamp == 0) {
            return "N/A";
        }

        long now = System.currentTimeMillis();
        long diff = now - timestamp;

        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days > 0) {
            return days + " day" + (days > 1 ? "s" : "") + " ago";
        } else if (hours > 0) {
            return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
        } else if (minutes > 0) {
            return minutes + " minute" + (minutes > 1 ? "s" : "") + " ago";
        } else {
            return "Just now";
        }
    }

    private void setStatusColor(TextView textView, String status) {
        int color;
        int backgroundColor;

        switch (status.toLowerCase()) {
            case "in_stock":
                color = 0xFF4CAF50; // Green
                textView.setText("In Stock");
                break;
            case "low_stock":
                color = 0xFFFFA726; // Orange
                textView.setText("Low Stock");
                break;
            case "out_of_stock":
                color = 0xFFF44336; // Red
                textView.setText("Out of Stock");
                break;
            default:
                color = 0xFF757575; // Grey
                textView.setText(status);
                break;
        }
        textView.setTextColor(color);
    }

    private String capitalizeFirst(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    static class MaterialViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMaterialIcon;
        TextView tvMaterialName, tvStockStatus, tvCategory, tvQuantity,
                 tvPrice, tvLastUpdated, tvSupplier;

        public MaterialViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMaterialIcon = itemView.findViewById(R.id.ivMaterialIcon);
            tvMaterialName = itemView.findViewById(R.id.tvMaterialName);
            tvStockStatus = itemView.findViewById(R.id.tvStockStatus);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvLastUpdated = itemView.findViewById(R.id.tvLastUpdated);
            tvSupplier = itemView.findViewById(R.id.tvSupplier);
        }
    }
}
