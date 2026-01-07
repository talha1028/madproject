package com.example.madproject.firebase;

import com.example.madproject.models.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class TaskManager {
    private static TaskManager instance;
    private final FirebaseFirestore db;
    private static final String COLLECTION_NAME = "tasks";

    private TaskManager() {
        db = FirebaseFirestore.getInstance();
    }

    public static synchronized TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    // CREATE - Add new task
    public com.google.android.gms.tasks.Task<Void> createTask(com.example.madproject.models.Task task) {
        return db.collection(COLLECTION_NAME)
                .document(task.getTaskId())
                .set(task);
    }

    // READ - Get single task by ID
    public com.google.android.gms.tasks.Task<DocumentSnapshot> getTask(String taskId) {
        return db.collection(COLLECTION_NAME)
                .document(taskId)
                .get();
    }

    // READ - Get all tasks for a job
    public com.google.android.gms.tasks.Task<QuerySnapshot> getTasksByJob(String jobId) {
        return db.collection(COLLECTION_NAME)
                .whereEqualTo("jobId", jobId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get();
    }

    // READ - Get tasks by status
    public com.google.android.gms.tasks.Task<QuerySnapshot> getTasksByStatus(String jobId, String status) {
        return db.collection(COLLECTION_NAME)
                .whereEqualTo("jobId", jobId)
                .whereEqualTo("status", status)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get();
    }

    // READ - Get ongoing tasks
    public com.google.android.gms.tasks.Task<QuerySnapshot> getOngoingTasks(String jobId) {
        return getTasksByStatus(jobId, "ongoing");
    }

    // READ - Get completed tasks
    public com.google.android.gms.tasks.Task<QuerySnapshot> getCompletedTasks(String jobId) {
        return getTasksByStatus(jobId, "completed");
    }

    // UPDATE - Update entire task
    public com.google.android.gms.tasks.Task<Void> updateTask(com.example.madproject.models.Task task) {
        task.setUpdatedAt(System.currentTimeMillis());
        return db.collection(COLLECTION_NAME)
                .document(task.getTaskId())
                .set(task);
    }

    // UPDATE - Update specific field
    public com.google.android.gms.tasks.Task<Void> updateField(String taskId, String field, Object value) {
        return db.collection(COLLECTION_NAME)
                .document(taskId)
                .update(
                        field, value,
                        "updatedAt", System.currentTimeMillis()
                );
    }

    // UPDATE - Update task status
    public com.google.android.gms.tasks.Task<Void> updateTaskStatus(String taskId, String status) {
        return updateField(taskId, "status", status);
    }

    // UPDATE - Update progress
    public com.google.android.gms.tasks.Task<Void> updateProgress(String taskId, double completedQuantity) {
        return db.collection(COLLECTION_NAME)
                .document(taskId)
                .get()
                .continueWithTask(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        com.example.madproject.models.Task taskObj = task.getResult().toObject(com.example.madproject.models.Task.class);
                        if (taskObj != null) {
                            taskObj.setCompletedQuantity(completedQuantity);
                            taskObj.calculateProgress();
                            return updateTask(taskObj);
                        }
                    }
                    return com.google.android.gms.tasks.Tasks.forException(new Exception("Task not found"));
                });
    }

    // UPDATE - Mark task as complete
    public com.google.android.gms.tasks.Task<Void> completeTask(String taskId) {
        return db.collection(COLLECTION_NAME)
                .document(taskId)
                .update(
                        "status", "completed",
                        "progressPercentage", 100.0,
                        "updatedAt", System.currentTimeMillis()
                );
    }

    // DELETE - Delete task
    public com.google.android.gms.tasks.Task<Void> deleteTask(String taskId) {
        return db.collection(COLLECTION_NAME)
                .document(taskId)
                .delete();
    }

    // QUERY - Get tasks by date range
    public com.google.android.gms.tasks.Task<QuerySnapshot> getTasksByDateRange(String jobId, long startDate, long endDate) {
        return db.collection(COLLECTION_NAME)
                .whereEqualTo("jobId", jobId)
                .whereGreaterThanOrEqualTo("startDate", startDate)
                .whereLessThanOrEqualTo("endDate", endDate)
                .get();
    }
}