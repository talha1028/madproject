package com.example.madproject.firebase;

import com.example.madproject.models.Material;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class MaterialManager {
    private static MaterialManager instance;
    private final FirebaseFirestore db;
    private static final String COLLECTION_NAME = "materials";

    private MaterialManager() {
        db = FirebaseFirestore.getInstance();
    }

    public static synchronized MaterialManager getInstance() {
        if (instance == null) {
            instance = new MaterialManager();
        }
        return instance;
    }

    // CREATE - Add new material
    public Task<Void> createMaterial(Material material) {
        return db.collection(COLLECTION_NAME)
                .document(material.getMaterialId())
                .set(material);
    }

    // READ - Get single material by ID
    public Task<DocumentSnapshot> getMaterial(String materialId) {
        return db.collection(COLLECTION_NAME)
                .document(materialId)
                .get();
    }

    // READ - Get all materials for a job
    public Task<QuerySnapshot> getMaterialsByJob(String jobId) {
        return db.collection(COLLECTION_NAME)
                .whereEqualTo("jobId", jobId)
                .orderBy("addedDate", Query.Direction.DESCENDING)
                .get();
    }

    // READ - Get materials by category
    public Task<QuerySnapshot> getMaterialsByCategory(String jobId, String category) {
        return db.collection(COLLECTION_NAME)
                .whereEqualTo("jobId", jobId)
                .whereEqualTo("category", category)
                .get();
    }

    // READ - Get materials by status
    public Task<QuerySnapshot> getMaterialsByStatus(String jobId, String status) {
        return db.collection(COLLECTION_NAME)
                .whereEqualTo("jobId", jobId)
                .whereEqualTo("status", status)
                .get();
    }

    // READ - Get low stock materials
    public Task<QuerySnapshot> getLowStockMaterials(String jobId) {
        return getMaterialsByStatus(jobId, "low_stock");
    }

    // READ - Get out of stock materials
    public Task<QuerySnapshot> getOutOfStockMaterials(String jobId) {
        return getMaterialsByStatus(jobId, "out_of_stock");
    }

    // UPDATE - Update entire material
    public Task<Void> updateMaterial(Material material) {
        material.setLastUpdated(System.currentTimeMillis());
        return db.collection(COLLECTION_NAME)
                .document(material.getMaterialId())
                .set(material);
    }

    // UPDATE - Update specific field
    public Task<Void> updateField(String materialId, String field, Object value) {
        return db.collection(COLLECTION_NAME)
                .document(materialId)
                .update(
                        field, value,
                        "lastUpdated", System.currentTimeMillis()
                );
    }

    // UPDATE - Update quantity
    public void updateQuantity(String materialId, double newQuantity) {
        db.collection(COLLECTION_NAME)
                .document(materialId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Material material = documentSnapshot.toObject(Material.class);
                        if (material != null) {
                            material.setQuantity(newQuantity);
                            updateMaterial(material);
                        }
                    }
                });
    }

    // UPDATE - Add quantity (restock)
    public void addQuantity(String materialId, double addedQuantity) {
        db.collection(COLLECTION_NAME)
                .document(materialId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Material material = documentSnapshot.toObject(Material.class);
                        if (material != null) {
                            material.setQuantity(material.getQuantity() + addedQuantity);
                            updateMaterial(material);
                        }
                    }
                });
    }

    // UPDATE - Deduct quantity (usage)
    public void deductQuantity(String materialId, double usedQuantity) {
        db.collection(COLLECTION_NAME)
                .document(materialId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Material material = documentSnapshot.toObject(Material.class);
                        if (material != null) {
                            material.setQuantity(material.getQuantity() - usedQuantity);
                            updateMaterial(material);
                        }
                    }
                });
    }

    // DELETE - Delete material
    public Task<Void> deleteMaterial(String materialId) {
        return db.collection(COLLECTION_NAME)
                .document(materialId)
                .delete();
    }

    // QUERY - Calculate total inventory value
    public void calculateTotalInventoryValue(String jobId, OnTotalCalculatedListener listener) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("jobId", jobId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    double totalValue = 0.0;
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Material material = doc.toObject(Material.class);
                        if (material != null) {
                            totalValue += material.getTotalCost();
                        }
                    }
                    listener.onTotalCalculated(totalValue);
                });
    }

    // Callback interface
    public interface OnTotalCalculatedListener {
        void onTotalCalculated(double totalValue);
    }
}