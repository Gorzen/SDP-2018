package ch.epfl.sweng.studyup.imagePathGetter;


import android.app.Activity;
import android.content.Intent;

public class pathFromGalleryGetter implements imagePathGetter {
    private final Activity activity;
    private final int requestCode;

    /**
     * Constructor storing the activity needed by the intent and the request code
     *
     * @param activity the activity from where the intent will be launched
     * @param requestCode the code to identify the intent launched
     */
    pathFromGalleryGetter(Activity activity, int requestCode) {
        this.activity = activity;
        this.requestCode = requestCode;
    }

    /**
     * Launch the gallery for the user to choose the image, using the activity
     * it is called from
     */
    @Override
    public void getFilePath() {
        // TODO: Not compatible with API < 19 (our minAPI is 15)
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened"
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        intent.setType("image/*");

        activity.startActivityForResult(intent, requestCode);
    }
}
