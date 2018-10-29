package ch.epfl.sweng.studyup.firebase;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import ch.epfl.sweng.studyup.utils.Utils;

import java.io.File;

public class FileStorage {

    public static StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    // Use this method to upload files to the Firebase Storage server
    // Arguments:
    //  destFilePath: path for file on server
    //  file: a File object for the file you would like to upload
    private static void uploadFile(String destFilePath, File file) {

        Uri fileURI = Uri.fromFile(file);

        StorageReference fileRef = storageRef.child(destFilePath);
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

    public static void uploadProblemImage(File file) {

        Uri fileURI = Uri.fromFile(file);
        String destFilePath = Utils.question_images_directory_name + "/" + fileURI.getLastPathSegment();

        uploadFile(destFilePath, file);
    }

    public static void uploadProfilePicture(String sciper, File file) {

        String destFilePath = Utils.profile_pictures_directory_name + sciper + ".png";

        uploadFile(destFilePath, file);
    }

    public static StorageReference getProblemImageRef(Uri fileURI) {

        StorageReference fileRef = storageRef.child(Utils.question_images_directory_name + "/" + fileURI.getLastPathSegment());
        return fileRef;
    }

    public StorageReference getProfilePictureRef(String sciper) {

        StorageReference fileRef = storageRef.child(Utils.profile_pictures_directory_name + "/" + sciper + ".png");
        return fileRef;
    }
}
