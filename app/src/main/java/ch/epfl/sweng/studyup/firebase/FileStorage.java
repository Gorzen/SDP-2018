package ch.epfl.sweng.studyup.firebase;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;

import static ch.epfl.sweng.studyup.utils.Constants.PROFILE_PICTURES_DIRECTORY_NAME;
import static ch.epfl.sweng.studyup.utils.Constants.QUESTION_IMAGES_DIRECTORY_NAME;

public class FileStorage {

    public static StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    private static void uploadFile(String dirName, File file) {

        Uri fileURI = Uri.fromFile(file);
        String destFilePath = dirName + "/" + fileURI.getLastPathSegment();

        StorageReference fileRef = storageRef.child(destFilePath);
        UploadTask uploadTask = fileRef.putFile(fileURI);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("Unable to upload image");
                exception.printStackTrace();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Successfully uploaded image");
            }
        });
    }


    public static void uploadProblemImage(File file) {
        uploadFile(QUESTION_IMAGES_DIRECTORY_NAME, file);
    }

    public static void uploadProfilePicture(File file) {
        uploadFile(PROFILE_PICTURES_DIRECTORY_NAME, file);
    }

    public static StorageReference getProblemImageRef(Uri fileURI) {

        return storageRef.child(QUESTION_IMAGES_DIRECTORY_NAME + "/" + fileURI.getLastPathSegment());
    }

    public static StorageReference getProfilePictureRef(String sciper) {

        return storageRef.child(PROFILE_PICTURES_DIRECTORY_NAME + "/" + sciper + ".png");

    }

    public static void downloadProfilePicture(String sciper, final ImageView image_view) {

        StorageReference ref = getProfilePictureRef(sciper);

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Pass the URL to Picasso to download and show in ImageView
                Picasso.get().load(uri.toString()).into(image_view);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.printStackTrace();
            }
        });
    }
}
