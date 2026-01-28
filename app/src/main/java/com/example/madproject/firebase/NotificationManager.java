package com.example.madproject.firebase;

import com.example.madproject.models.Notification;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class NotificationManager {
    private static NotificationManager instance;
    private final FirebaseFirestore db;
    private static final String COLLECTION_NAME = "notifications";

    private NotificationManager() {
        db = FirebaseFirestore.getInstance();
    }

    public static synchronized NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    // CREATE - Send notification
    public Task<Void> createNotification(Notification notification) {
        return db.collection(COLLECTION_NAME)
                .document(notification.getNotificationId())
                .set(notification);
    }

    // READ - Get single notification by ID
    public Task<DocumentSnapshot> getNotification(String notificationId) {
        return db.collection(COLLECTION_NAME)
                .document(notificationId)
                .get();
    }

    // READ - Get all notifications for user (sort in memory after fetching)
    public Task<QuerySnapshot> getNotificationsByUser(String userId) {
        return db.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .get();
    }

    // READ - Get unread notifications (sort in memory after fetching)
    public Task<QuerySnapshot> getUnreadNotifications(String userId) {
        return db.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .whereEqualTo("isRead", false)
                .get();
    }

    // READ - Get notifications by type (sort in memory after fetching)
    public Task<QuerySnapshot> getNotificationsByType(String userId, String type) {
        return db.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .whereEqualTo("type", type)
                .get();
    }

    // READ - Get unread count
    public void getUnreadCount(String userId, OnCountLoadedListener listener) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .whereEqualTo("isRead", false)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listener.onCountLoaded(queryDocumentSnapshots.size());
                });
    }

    // UPDATE - Mark as read
    public Task<Void> markAsRead(String notificationId) {
        return db.collection(COLLECTION_NAME)
                .document(notificationId)
                .update(
                        "isRead", true,
                        "readAt", System.currentTimeMillis()
                );
    }

    // UPDATE - Mark all as read
    public void markAllAsRead(String userId) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .whereEqualTo("isRead", false)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        markAsRead(doc.getId());
                    }
                });
    }

    // DELETE - Delete notification
    public Task<Void> deleteNotification(String notificationId) {
        return db.collection(COLLECTION_NAME)
                .document(notificationId)
                .delete();
    }

    // DELETE - Delete all notifications for user
    public void deleteAllNotifications(String userId) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        deleteNotification(doc.getId());
                    }
                });
    }

    // REAL-TIME - Listen to notifications (sort in memory after fetching)
    public com.google.firebase.firestore.ListenerRegistration listenToNotifications(String userId, OnNotificationsChangedListener listener) {
        return db.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        listener.onError(error.getMessage());
                        return;
                    }
                    if (value != null) {
                        listener.onNotificationsChanged(value);
                    }
                });
    }

    // Callback interfaces
    public interface OnCountLoadedListener {
        void onCountLoaded(int count);
    }

    public interface OnNotificationsChangedListener {
        void onNotificationsChanged(QuerySnapshot notifications);
        void onError(String error);
    }
}