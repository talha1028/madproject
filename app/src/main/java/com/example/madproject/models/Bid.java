package com.example.madproject.models;

import java.util.ArrayList;
import java.util.List;

public class Bid {
    private String bidId;
    private String jobId;
    private String jobTitle;
    private String contractorId;
    private String contractorName;
    private String contractorPhotoUrl;
    private String contractorCategory;
    private double contractorRating;
    private int contractorCompletedProjects;
    private double bidAmount;
    private int completionDays;
    private String proposal;
    private long submittedDate;
    private String status; // "pending", "accepted", "rejected"
    private List<String> portfolioImages;

    // Required empty constructor for Firestore
    public Bid() {
        this.portfolioImages = new ArrayList<>();
    }

    // Constructor
    public Bid(String bidId, String jobId, String jobTitle, String contractorId,
               String contractorName, double bidAmount, int completionDays, String proposal) {
        this.bidId = bidId;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.contractorId = contractorId;
        this.contractorName = contractorName;
        this.bidAmount = bidAmount;
        this.completionDays = completionDays;
        this.proposal = proposal;
        this.status = "pending";
        this.submittedDate = System.currentTimeMillis();
        this.portfolioImages = new ArrayList<>();
    }

    // Getters and Setters
    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
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

    public String getContractorPhotoUrl() {
        return contractorPhotoUrl;
    }

    public void setContractorPhotoUrl(String contractorPhotoUrl) {
        this.contractorPhotoUrl = contractorPhotoUrl;
    }

    public String getContractorCategory() {
        return contractorCategory;
    }

    public void setContractorCategory(String contractorCategory) {
        this.contractorCategory = contractorCategory;
    }

    public double getContractorRating() {
        return contractorRating;
    }

    public void setContractorRating(double contractorRating) {
        this.contractorRating = contractorRating;
    }

    public int getContractorCompletedProjects() {
        return contractorCompletedProjects;
    }

    public void setContractorCompletedProjects(int contractorCompletedProjects) {
        this.contractorCompletedProjects = contractorCompletedProjects;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }

    public int getCompletionDays() {
        return completionDays;
    }

    public void setCompletionDays(int completionDays) {
        this.completionDays = completionDays;
    }

    public String getProposal() {
        return proposal;
    }

    public void setProposal(String proposal) {
        this.proposal = proposal;
    }

    public long getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(long submittedDate) {
        this.submittedDate = submittedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getPortfolioImages() {
        return portfolioImages;
    }

    public void setPortfolioImages(List<String> portfolioImages) {
        this.portfolioImages = portfolioImages;
    }
}