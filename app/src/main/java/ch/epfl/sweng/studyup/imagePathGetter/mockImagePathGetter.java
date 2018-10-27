package ch.epfl.sweng.studyup.imagePathGetter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import ch.epfl.sweng.studyup.questions.AddQuestionActivity;

public class mockImagePathGetter implements imagePathGetter {
    public static final Uri fakeUri = Uri.parse("studyup://fake/path");
    private final Activity activity;
    private final int requestCode;

    /**
     * Constructor storing the activity that use this mock and the code used to identify the
     * intent
     *
     * @param activity the activity that need the mock
     * @param requestCode the code to identify the intent
     */
    public mockImagePathGetter(Activity activity, int requestCode) {
        this.activity = activity;
        this.requestCode = requestCode;
    }

    @Override
    public void getFilePath() {
        Intent i = new Intent();
        i.setData(fakeUri);

        if(activity instanceof AddQuestionActivity) {
            ((AddQuestionActivity) activity).onActivityResult(requestCode, Activity.RESULT_OK, i);
        }
    }
}
