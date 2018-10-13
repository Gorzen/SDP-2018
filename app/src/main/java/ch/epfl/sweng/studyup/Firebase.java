package ch.epfl.sweng.studyup;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.Map;

public class Firebase {
    private static Firebase instance = null;
    private static FirebaseFirestore db;
    private static Map<String, Object> userData;
    private static boolean lastActionSuccess;

    private Firebase() {
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();

        // DB settings
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }

    public static Firebase get() {
        if(instance == null) {
            instance = new Firebase();
        }

        return instance;
    }

    public Map<String, Object> getUserData(final int sciper) throws IllegalArgumentException {
        if(sciper < 100000 || sciper > 999999) {
            throw new IllegalArgumentException("Error: getUserData, SCIPER number should be a six digits number.");
        }

        db.collection("users").document(Integer.toString(sciper))
            .get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        userData = document.getData();
                        System.out.println("Success: getUserData(" + sciper + ").");
                    } else {
                        System.out.println("Error: getUserData, no such user (" + sciper + ").");
                    }
                } else {
                    System.out.println("Error: getUserData, " + task.getException());
                }
            }
        });

        return userData;
    }

    public boolean setUserData(final int sciper, final String key, final Object value) throws IllegalArgumentException {
        if (sciper < 100000 || sciper > 999999) {
            throw new IllegalArgumentException("Error: setUserData, SCIPER number should be a six digits number.");
        }

        Map<String, Object> data = getUserData(sciper);
        data.put(key, value);

        db.collection("users").document(Integer.toString(sciper))
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        lastActionSuccess = true;
                        System.out.println("Succes: setUserData(" + sciper + ").");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        lastActionSuccess = false;
                        System.out.println("Error: setUserData, " + e.toString());
                    }
                });
        return lastActionSuccess;
    }
}


