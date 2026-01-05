package com.example.madproject.models;

import java.util.ArrayList;
import java.util.List;

public class Material {
    private String materialId;
    private String jobId;
    private String projectName;
    private String materialName;
    private String category; // "Cement", "Steel", "Bricks", "Sand", "Gravel", etc.
    private double quantity;
    private String unit; // "bags", "kg", "tons", "pieces", "cubic_meter"
    private double unitPrice;
    private double totalCost;
    private String supplier;
    private String supplierContact;
    private String description;
    private String status; // "in_stock", "low_stock", "out_of_stock"
    private double lowStockThreshold;
    private long addedDate;
    private long lastUpdated;
    private String addedBy;
    private List<String> photos;

    // Required empty constructor for Firestore
    public Material() {
        this.photos = new ArrayList<>();
    }

    // Constructor
    public Material(String materialId, String jobId, String projectName, String materialName,
                    String category, double quantity, String unit, double unitPrice, String supplier) {
        this.materialId = materialId;
        this.jobId = jobId;
        this.projectName = projectName;
        this.materialName = materialName;
        this.category = category;
        this.quantity = quantity;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.supplier = supplier;
        this.totalCost = quantity * unitPrice;
        this.status = "in_stock";
        this.addedDate = System.currentTimeMillis();
        this.lastUpdated = System.currentTimeMillis();
        this.photos = new ArrayList<>();
    }

    // Getters and Setters
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
        calculateTotalCost();
        checkStockStatus();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        calculateTotalCost();
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getSupplierContact() {
        return supplierContact;
    }

    public void setSupplierContact(String supplierContact) {
        this.supplierContact = supplierContact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getLowStockThreshold() {
        return lowStockThreshold;
    }

    public void setLowStockThreshold(double lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }

    public long getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(long addedDate) {
        this.addedDate = addedDate;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    // Helper methods
    private void calculateTotalCost() {
        this.totalCost = this.quantity * this.unitPrice;
    }

    private void checkStockStatus() {
        if (this.quantity <= 0) {
            this.status = "out_of_stock";
        } else if (this.lowStockThreshold > 0 && this.quantity <= this.lowStockThreshold) {
            this.status = "low_stock";
        } else {
            this.status = "in_stock";
        }
    }
}