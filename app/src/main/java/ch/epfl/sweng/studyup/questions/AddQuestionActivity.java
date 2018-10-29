package ch.epfl.sweng.studyup.questions;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.utils.imagePathGetter.imagePathGetter;
import ch.epfl.sweng.studyup.utils.imagePathGetter.mockImagePathGetter;
import ch.epfl.sweng.studyup.utils.imagePathGetter.pathFromGalleryGetter;
import ch.epfl.sweng.studyup.utils.Utils;
import ch.epfl.sweng.studyup.firebase.FileStorage;
import ch.epfl.sweng.studyup.firebase.Firestore;

public class AddQuestionActivity extends AppCompatActivity {

    private static final String TAG = "AddQuestionActivity";

    private static final int READ_REQUEST_CODE = 42;
    private Uri imageURI = null;
    private RadioGroup trueFalseRadioGroup;
    private imagePathGetter getPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        addRadioListener();

        if(Utils.isMockEnabled) {
            getPath = new mockImagePathGetter(this, READ_REQUEST_CODE);
        } else {
            getPath = new pathFromGalleryGetter(this, READ_REQUEST_CODE);
        }
    }

    /**
     * Function called when the user wants to choose an image in gallery
     *
     * @param current the current view
     */
    public void performFileSearch(View current) {
        getPath.getFilePath();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            if (resultData != null) {
                imageURI = resultData.getData();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView displayName = findViewById(R.id.display_question_path);
                        displayName.setText(imageURI.toString());
                    }
                });
            }
        }
    }

    public void addQuestion(View current) {
        if (imageURI != null) {
            RadioGroup answerGroup = findViewById(R.id.question_radio_group);
            RadioButton checkedButton = findViewById(answerGroup.getCheckedRadioButtonId());
            //get the tag of the button to know the answer number
            int answerNumber = Integer.parseInt(checkedButton.getTag().toString()) - 1;

            boolean isTrueFalseQuestion = trueFalseRadioGroup.getCheckedRadioButtonId() == R.id.true_false_radio;

            String newQuestionID = UUID.randomUUID().toString();
            File questionFile = new File(this.getApplicationContext().getFilesDir(), newQuestionID + ".png");
            try {
                Bitmap imageBitmap = getBitmapFromUri(imageURI);
                FileOutputStream out = new FileOutputStream(questionFile);
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }

            // Upload the problem image file to the Firebase Storage server
            FileStorage.uploadProblemImage(questionFile);

            EditText newQuestionTitleView = findViewById(R.id.questionTitle);
            String newQuestionTitle = newQuestionTitleView.getText().toString();
            Question q = new Question(newQuestionID, newQuestionTitle, isTrueFalseQuestion, answerNumber);

            // Add question to FireStore
            Firestore.addQuestion(q);

            //ArrayList<Question> list = new ArrayList<>();
            //list.add(q);
            //QuestionParser.writeQuestions(list, this.getApplicationContext());
            Toast.makeText(this.getApplicationContext(), "Question added !", Toast.LENGTH_SHORT).show();
        }
    }

    private void addRadioListener() {
        trueFalseRadioGroup = findViewById(R.id.true_false_or_mcq_radio_group);
        trueFalseRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton firstRadioButton = findViewById(R.id.radio_answer1);
                RadioButton secondRadioButton = findViewById(R.id.radio_answer2);
                RadioButton thirdRadioButton = findViewById(R.id.radio_answer3);
                RadioButton fourthRadioButton = findViewById(R.id.radio_answer4);

                if (checkedId == R.id.true_false_radio) {
                    //mask the 3rd and 4th radio button and uncheck them
                    thirdRadioButton.setVisibility(View.INVISIBLE);
                    thirdRadioButton.setChecked(false);
                    fourthRadioButton.setVisibility(View.INVISIBLE);
                    fourthRadioButton.setChecked(false);

                    //Change the text to the 1st and second button to True and False
                    firstRadioButton.setText(R.string.truth_value);
                    secondRadioButton.setText(R.string.false_value);

                } else {
                    //unmask the last two buttons and set the text to the first ones to numbers
                    thirdRadioButton.setVisibility(View.VISIBLE);
                    thirdRadioButton.setChecked(false);
                    fourthRadioButton.setVisibility(View.VISIBLE);
                    fourthRadioButton.setChecked(false);
                    firstRadioButton.setChecked(true);

                    //Change the text to the 1st and second button to True and False
                    firstRadioButton.setText("1");
                    secondRadioButton.setText("2");
                }
            }
        });
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}
