package com.example.madproject.models;

import java.util.ArrayList;
import java.util.List;

public class Review {
    private String reviewId;
    private String contractorId;
    private String contractorName;
    private String clientId;
    private String clientName;
    private String clientPhotoUrl;
    private String jobId;
    private String jobTitle;
    private float rating; // 1-5 stars
    private String reviewText;
    private long reviewDate;
    private List<String> photos;
    private boolean isVerified; // Was this a real completed job?
    private String response; // Contractor's response to review
    private long responseDate;

    // Required empty constructor for Firestore
    public Review() {
        this.photos = new ArrayList<>();
    }

    // Constructor
    public Review(String reviewId, String contractorId, String contractorName,
                  String clientId, String clientName, String jobId, String jobTitle,
                  float rating, String reviewText) {
        this.reviewId = reviewId;
        this.contractorId = contractorId;
        this.contractorName = contractorName;
        this.clientId = clientId;
        this.clientName = clientName;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.rating = rating;
        this.reviewText = reviewText;
        this.reviewDate = System.currentTimeMillis();
        this.photos = new ArrayList<>();
        this.isVerified = true; // Set based on job completion
    }

    // Getters and Setters
    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getContractorId() {
        return contractorId;
    }

    public void setContractorId(String contractorId) {
        this.contractorId = contractorId;
    }

    public String getContractorName() {
        return contractorName;
    }

    public void setContractorName(String contractorName) {
        this.contractorName = contractorName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPhotoUrl() {
        return clientPhotoUrl;
    }

    public void setClientPhotoUrl(String clientPhotoUrl) {
        this.clientPhotoUrl = clientPhotoUrl;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public long getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(long reviewDate) {
        this.reviewDate = reviewDate;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
        if (response != null && !response.isEmpty()) {
            this.responseDate = System.currentTimeMillis();
        }
    }

    public long getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(long responseDate) {
        this.responseDate = responseDate;
    }

    // Helper method to get star display
    public String getStarDisplay() {
        StringBuilder stars = new StringBuilder();
        int fullStars = (int) rating;
        boolean hasHalfStar = (rating - fullStars) >= 0.5;

        for (int i = 0; i < fullStars; i++) {
            stars.append("★");
        }
        if (hasHalfStar) {
            stars.append("½");
        }
        int remainingStars = 5 - fullStars - (hasHalfStar ? 1 : 0);
        for (int i = 0; i < remainingStars; i++) {
            stars.append("☆");
        }
        return stars.toString();
    }
}