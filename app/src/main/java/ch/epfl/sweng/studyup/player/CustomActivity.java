package ch.epfl.sweng.studyup.player;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.social.RankingsActivity;
import ch.epfl.sweng.studyup.utils.Navigation;
import ch.epfl.sweng.studyup.utils.Utils;

/**
 * CustomActivity
 *
 * Code used in the activity_custom to personnalize a Player.
 */
public class CustomActivity extends Navigation {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 8826229;
    private static final String IMAGE_DIRECTORY = "/studyup_photos";
    private static final int GALLERY = 0, CAMERA = 1;
    private ImageView imageview;
    private ImageButton pic_button;
    private ImageButton valid_button;
    private EditText edit_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        navigationSwitcher(CustomActivity.this, CustomActivity.class, Utils.DEFAULT_INDEX);

        pic_button = findViewById(R.id.pic_btn);
        valid_button = findViewById(R.id.valid_btn);
        imageview = findViewById(R.id.pic_imageview);
        edit_username = findViewById(R.id.edit_username);

        final TextView view_username = findViewById(R.id.view_username);//todo

        // TODO: Change with the user pic
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user_init_pic);
        RoundedBitmapDrawable rbd = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        rbd.setCircular(true);
        imageview.setImageDrawable(rbd);
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
                view_username.setText(edit_username.getText().toString());
            }
        });

    }

    private void selectImage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CustomActivity.this);
        dialogBuilder.setTitle("Add an image");
        final String[] items = {"Gallery", "Camera", "Cancel"};

        dialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == GALLERY) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, GALLERY);
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
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Can't take photos without permission.",
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
                    setCircularImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(CustomActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == CAMERA) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            setCircularImage(bitmap);
        }
    }

    //Not sure about the name-consistency of this function
    private void setCircularImage(Bitmap bitmap) {
        //saveImage(bitmap);
        RoundedBitmapDrawable rbd = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        rbd.setCircular(true);
        Toast.makeText(CustomActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        imageview.setImageDrawable(rbd);
    }
  
    private String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
            return f.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
      
    //Display the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.top_navigation, menu);
        return true;
    }

    //Allows you to do an action with the toolbar (in a different way than with the navigation bar)
    //Corresponding activities are not created yet
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.top_navigation_settings) {
            Toast.makeText(CustomActivity.this,
                    "You have clicked on Settings :)",
                    Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.top_navigation_infos) {
            Toast.makeText(CustomActivity.this,
                    "You have clicked on Infos :)",
                    Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
