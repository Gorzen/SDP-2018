package ch.epfl.sweng.studyup.firebase;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class FirebaseCloud {

    public static StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    // Use this method to upload files to the Firebase Storage server
    // Arguments:
    //  directory: directory name in server, use string values specified in strings.xml according to file type
    //  file: a File object for the file you would like to upload
    public static void uploadFile(String directory, File file) {

        Uri fileURI = Uri.fromFile(file);

        StorageReference fileRef = storageRef.child(directory + "/" + fileURI.getLastPathSegment());
        UploadTask uploadTask = fileRef.putFile(fileURI);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("Unable to upload problem image");
                exception.printStackTrace();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Successfully uploaded problem image");
            }
        });
    }

    // Use this method to get a StorageReference object for a given file
    // Call getBytes() or getStream() on the storage reference to use the corresponding file
    //
    // Arguments:
    //  directory: directory name in server, use string values specified in strings.xml according to file type
    //  fileName: the name of the file you wish to get StorageReference for
    public static StorageReference getFileStorageRef(String directory, String fileName) {

        StorageReference fileRef = storageRef.child(directory + "/" + fileName);
        return fileRef;
    }
}