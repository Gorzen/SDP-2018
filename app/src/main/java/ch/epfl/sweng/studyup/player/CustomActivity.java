package ch.epfl.sweng.studyup.player;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Normalizer;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.FileStorage;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.navigation.NavigationStudent;

import static ch.epfl.sweng.studyup.utils.Constants.DEFAULT_INDEX_STUDENT;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;


/**
 * CustomActivity
 *
 * Code used in the activity_custom to personnalize a Player.
 */
public class CustomActivity extends NavigationStudent {
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String TAG = "CustomActivity";
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 8826229;
    private static final int GALLERY = 0, CAMERA = 1;
    private ImageView imageview;
    private EditText edit_username;
    public ImageButton valid_button;

    @SuppressWarnings("HardCodedStringLiteral")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_custom);
        super.onCreate(savedInstanceState);

        navigationSwitcher(CustomActivity.this, CustomActivity.class, DEFAULT_INDEX_STUDENT);

        ImageButton pic_button = findViewById(R.id.pic_btn);
        valid_button = findViewById(R.id.valid_btn);
        imageview = findViewById(R.id.pic_imageview);
        edit_username = findViewById(R.id.edit_username);
        edit_username.setText(Player.get().getUserName());
        final TextView user_email = findViewById(R.id.user_email);

        //mail
        String email_1 = Player.get().getFirstName().split(" ")[0].toLowerCase();
        email_1 = Normalizer.normalize(email_1, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        String email_2 = Player.get().getLastName().split(" ")[0].toLowerCase();
        email_2 = Normalizer.normalize(email_2, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        user_email.setText(email_1+ "."+ email_2 +"@epfl.ch");

        //initial picture
        FileStorage.downloadProfilePicture(Player.get().getSciperNum(), imageview);


        pic_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        valid_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valid_button.setBackground(getResources().getDrawable(R.drawable.ic_check_done_24dp));
                Toast.makeText(getApplicationContext(), getString(R.string.text_saved), Toast.LENGTH_SHORT).show();
                Player.get().setUserName(edit_username.getText().toString());
            }
        });

    }

    private void selectImage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CustomActivity.this);
        dialogBuilder.setTitle(getString(R.string.text_addimage));
        final String[] items = {getString(R.string.gallery), getString(R.string.camera), getString(R.string.cancel)};

        dialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == GALLERY) {
                    openGallery();
                } else if (which == CAMERA) {
                    if (checkSelfPermission(Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED) {
                        openCamera();
                    } else {
                        // We are here if we dont already have permission to invoke the camera,
                        // although we have the permission in the manifest
                        // We now have to override the method invoked for permissions:
                        // onRequestPermissionsResult()
                        String[] permissionRequest = {Manifest.permission.CAMERA};
                        requestPermissions(permissionRequest, CAMERA_PERMISSION_REQUEST_CODE);
                    }
                } else {
                    dialog.dismiss();
                }
            }
        });
        dialogBuilder.show();
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (!MOCK_ENABLED) {
            startActivityForResult(intent, CAMERA);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (!MOCK_ENABLED) {
            startActivityForResult(intent, GALLERY);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, getString(R.string.text_photospermissions),
                        Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    setImageCircularAndUpload(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(CustomActivity.this, getString(R.string.text_failed), Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == CAMERA) {
            @SuppressWarnings("HardCodedStringLiteral")
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            setImageCircularAndUpload(bitmap);
        }
    }

    public void setImageCircularAndUpload(Bitmap bitmap) {
        @SuppressWarnings("HardCodedStringLiteral")
        String newPictureFileID = Player.get().getSciperNum() + ".png";
        File pictureFile = new File(this.getApplicationContext().getFilesDir(), newPictureFileID);
        try {
            FileOutputStream out = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        FileStorage.uploadProfilePicture(pictureFile);

        RoundedBitmapDrawable rbd = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        rbd.setCircular(true);

        Toast.makeText(CustomActivity.this, getString(R.string.text_saved), Toast.LENGTH_SHORT).show();
        imageview.setImageDrawable(rbd);
    }
}
