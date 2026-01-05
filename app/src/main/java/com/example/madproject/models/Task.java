package com.example.madproject.models;

import java.util.ArrayList;
import java.util.List;

public class Task {
    private String taskId;
    private String jobId;
    private String projectName;
    private String taskTitle;
    private String description;
    private String assignedTo; // Worker/contractor name
    private int numberOfWorkers;
    private long startDate;
    private long endDate;
    private String status; // "not_started", "ongoing", "completed"
    private double progressPercentage;
    private String progressUnit; // "sqft", "cubic_meter", "pieces", "bags", etc.
    private double estimatedQuantity;
    private double completedQuantity;
    private double dailyWages;
    private double totalCost;
    private List<String> photos;
    private long createdAt;
    private long updatedAt;
    private String createdBy;

    // Required empty constructor for Firestore
    public Task() {
        this.photos = new ArrayList<>();
    }

    // Constructor
    public Task(String taskId, String jobId, String projectName, String taskTitle,
                String description, String assignedTo, int numberOfWorkers) {
        this.taskId = taskId;
        this.jobId = jobId;
        this.projectName = projectName;
        this.taskTitle = taskTitle;
        this.description = description;
        this.assignedTo = assignedTo;
        this.numberOfWorkers = numberOfWorkers;
        this.status = "not_started";
        this.progressPercentage = 0.0;
        this.completedQuantity = 0.0;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.photos = new ArrayList<>();
    }

    // Getters and Setters
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public int getNumberOfWorkers() {
        return numberOfWorkers;
    }

    public void setNumberOfWorkers(int numberOfWorkers) {
        this.numberOfWorkers = numberOfWorkers;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public String getProgressUnit() {
        return progressUnit;
    }

    public void setProgressUnit(String progressUnit) {
        this.progressUnit = progressUnit;
    }

    public double getEstimatedQuantity() {
        return estimatedQuantity;
    }

    public void setEstimatedQuantity(double estimatedQuantity) {
        this.estimatedQuantity = estimatedQuantity;
    }

    public double getCompletedQuantity() {
        return completedQuantity;
    }

    public void setCompletedQuantity(double completedQuantity) {
        this.completedQuantity = completedQuantity;
    }

    public double getDailyWages() {
        return dailyWages;
    }

    public void setDailyWages(double dailyWages) {
        this.dailyWages = dailyWages;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    // Helper method to calculate progress
    public void calculateProgress() {
        if (estimatedQuantity > 0) {
            this.progressPercentage = (completedQuantity / estimatedQuantity) * 100;
            if (this.progressPercentage >= 100) {
                this.progressPercentage = 100;
                this.status = "completed";
            }
        }
    }
}