package ch.epfl.sweng.studyup.questions;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

import ch.epfl.sweng.studyup.LoginActivity;
import ch.epfl.sweng.studyup.MainActivity;
import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.FileStorage;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.imagePathGetter.imagePathGetter;
import ch.epfl.sweng.studyup.utils.imagePathGetter.mockImagePathGetter;
import ch.epfl.sweng.studyup.utils.imagePathGetter.pathFromGalleryGetter;
import ch.epfl.sweng.studyup.utils.navigation.NavigationTeacher;

import static ch.epfl.sweng.studyup.utils.Constants.ADD_QUESTION_INDEX;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_UUID;

public class AddQuestionActivity extends NavigationTeacher {

    private static final String TAG = "AddQuestionActivity";

    private static final int READ_REQUEST_CODE = 42;
    private Uri imageURI = null;
    private Bitmap bitmap = null;
    private RadioGroup trueFalseRadioGroup;
    private RadioGroup imageTextRadioGroup;
    private imagePathGetter getPath;
    private Button logout_button;
    private int answer = 1;
    private boolean isNewQuestion = true;
    private Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        Intent intent = getIntent();
        Question question = (Question)intent.getSerializableExtra(AddQuestionActivity.class.getSimpleName());
        Log.d("TEST_EDIT_QUESTION", "question = " + question);
        if(question != null) {
            ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);

            this.question = question;
            answer = question.getAnswer();
            isNewQuestion = false;
            int trueFalseOrMCQ = question.isTrueFalse() == true ? R.id.true_false_radio : R.id.mcq_radio;
            setupEditQuestion(trueFalseOrMCQ);
        }

        if (!MOCK_ENABLED) {
            Firestore.get().loadQuestions(this);
        }

        navigationSwitcher(AddQuestionActivity.this, AddQuestionActivity.class, ADD_QUESTION_INDEX);

        addRadioListener();

        if (MOCK_ENABLED) {
            getPath = new mockImagePathGetter(this, READ_REQUEST_CODE);
        } else {
            getPath = new pathFromGalleryGetter(this, READ_REQUEST_CODE);
        }

        logout_button = findViewById(R.id.logoutbutton);

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.clearCacheToLogOut(AddQuestionActivity.this);
                Intent intent = new Intent(AddQuestionActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.go_right_in, R.anim.go_right_out);
            }
        });
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
                        ImageView displayImage = findViewById(R.id.addQuestion_display_image);
                        try {
                            Bitmap image_bitmap = getBitmapFromUri(imageURI);
                            displayImage.setImageBitmap(image_bitmap);
                            TextView displayName = findViewById(R.id.display_question_path);
                            displayName.setVisibility(View.GONE);
                        } catch (IOException e) {
                            Log.e(TAG, "An error occurred when displaying the image");
                        }
                    }
                });
            }
        }
    }

    public void addQuestion(View current) {
        if (imageURI != null || imageTextRadioGroup.getCheckedRadioButtonId() == R.id.text_radio_button) {
            RadioGroup answerGroup = findViewById(R.id.question_radio_group);
            RadioButton checkedButton = findViewById(answerGroup.getCheckedRadioButtonId());
            //get the tag of the button to know the answer number
            int answerNumber = Integer.parseInt(checkedButton.getTag().toString()) - 1;

            boolean isTrueFalseQuestion = trueFalseRadioGroup.getCheckedRadioButtonId() == R.id.true_false_radio;

            String newQuestionID = isNewQuestion ? getUUID() : question.getQuestionId();

            EditText newQuestionTitleView = findViewById(R.id.questionTitle);
            String newQuestionTitle = newQuestionTitleView.getText().toString();
            if (newQuestionTitle.isEmpty()) return;

            RadioGroup imageTextRadioGroup = findViewById(R.id.text_or_image_radio_group);
            File questionFile = null;

            if (imageTextRadioGroup.getCheckedRadioButtonId() == R.id.image_radio_button) {
                questionFile = new File(this.getApplicationContext().getFilesDir(), newQuestionID + ".png");
                try {
                    Bitmap imageBitmap = bitmap == null ? getBitmapFromUri(imageURI) : bitmap;
                    FileOutputStream out = new FileOutputStream(questionFile);
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            } else {
                try {
                    Log.e(TAG, "text selected write file");
                    questionFile = new File(this.getApplicationContext().getFilesDir(), newQuestionID + ".txt");
                    FileWriter writer = new FileWriter(questionFile);
                    TextView questionTextView = findViewById(R.id.questionText);
                    String questionData = questionTextView.getText().toString();
                    if (questionData.isEmpty()) return;
                    writer.write(questionData);
                    writer.close();
                } catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }
            }

            Log.e(TAG, "create the question");
            if (newQuestionTitle.length() == 0) return;

            // TODO: Determine how to set question course
            String questionCourseName = Player.get().getCourses().get(0).name();
            Question newQuestion = new Question(newQuestionID, newQuestionTitle, isTrueFalseQuestion, answerNumber, questionCourseName);

            // Upload the problem image file to the Firebase Storage server
            FileStorage.uploadProblemImage(questionFile);
            // Add question to FireStore
            Firestore.get().addQuestion(newQuestion);

            Toast.makeText(this.getApplicationContext(), "Question added !", Toast.LENGTH_SHORT).show();
        }
    }

    private String getUUID() {
        if (MOCK_ENABLED) {
            return MOCK_UUID;
        } else {
            return UUID.randomUUID().toString();
        }
    }

    /**
     * Sets the MCQ or True/False Radio Buttons. This method is used when a question is being edited
     * to display the corresponding checked radio buttons and is also used when the radio listener is being set
     * @param trueFalseOrMCQID chooses between MCQ or True/False alternatives
     */
    private void setUpMCQTrueFalseRadioButtons(int trueFalseOrMCQID) {
        RadioButton firstRadioButton = findViewById(R.id.radio_answer1);
        RadioButton secondRadioButton = findViewById(R.id.radio_answer2);
        RadioButton thirdRadioButton = findViewById(R.id.radio_answer3);
        RadioButton fourthRadioButton = findViewById(R.id.radio_answer4);

        if (trueFalseOrMCQID == R.id.true_false_radio) {
            //mask the 3rd and 4th radio button and uncheck them
            thirdRadioButton.setVisibility(View.INVISIBLE);
            thirdRadioButton.setChecked(false);
            fourthRadioButton.setVisibility(View.INVISIBLE);
            fourthRadioButton.setChecked(false);
            //Change the text to the 1st and second button to True and False
            firstRadioButton.setText(R.string.truth_value);
            secondRadioButton.setText(R.string.false_value);
            if(isNewQuestion && question.isTrueFalse()) {
                switch(answer) {
                    case 1 : firstRadioButton.setChecked(true);
                    case 2 : secondRadioButton.setChecked(true);
                }
            }

        } else {
            //unmask the last two buttons and set the text to the first ones to numbers
            thirdRadioButton.setVisibility(View.VISIBLE);
            thirdRadioButton.setChecked(false);
            fourthRadioButton.setVisibility(View.VISIBLE);
            fourthRadioButton.setChecked(false);
            if (isNewQuestion && !question.isTrueFalse()) {
                switch(answer) {
                    case 1 : firstRadioButton.setChecked(true);
                    case 2 : secondRadioButton.setChecked(true);
                    case 3 : thirdRadioButton.setChecked(true);
                    case 4 : fourthRadioButton.setChecked(true);
                }
            } else {
                firstRadioButton.setChecked(true);
            }
            //Change the text to the 1st and second button to True and False
            firstRadioButton.setText("1");
            secondRadioButton.setText("2");
        }
    }


    private void addRadioListener() {
        trueFalseRadioGroup = findViewById(R.id.true_false_or_mcq_radio_group);
        trueFalseRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setUpMCQTrueFalseRadioButtons(checkedId);
            }
        });

        imageTextRadioGroup = findViewById(R.id.text_or_image_radio_group);
        imageTextRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setsUpImageOrTextBasedRadioButtons(checkedId);
            }
        });
    }

    private void setsUpImageOrTextBasedRadioButtons(int imageOrTextQuestioniD) {
        Button selectImageButton = findViewById(R.id.selectImageButton);
        TextView displayPath = findViewById(R.id.display_question_path);
        ImageView imageQuestion = findViewById(R.id.addQuestion_display_image);
        TextView questionText = findViewById(R.id.questionText);
        if (imageOrTextQuestioniD == R.id.text_radio_button) {
            //mask everything related to the image-based question
            selectImageButton.setVisibility(View.GONE);
            displayPath.setVisibility(View.GONE);
            imageQuestion.setVisibility(View.GONE);
            //display the text for the question
            questionText.setVisibility(View.VISIBLE);
        } else {
            //show everything related to the image-based question
            selectImageButton.setVisibility(View.VISIBLE);
            displayPath.setVisibility(View.VISIBLE);
            imageQuestion.setVisibility(View.VISIBLE);
            //mask the text for the question
            questionText.setVisibility(View.GONE);
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    private void setUpQuestionTitle() {
        TextView questionTitle = findViewById(R.id.questionTitle);
        questionTitle.setText(question.getTitle());
    }

    private void setupEditQuestion(int trueFalseOrMCQId) {
        setUpQuestionTitle();
        setTrueFasleMCQRadioButtonFirstTime(trueFalseOrMCQId);
        setUpMCQTrueFalseRadioButtons(trueFalseOrMCQId);
        setupTextAndImage();
    }

    private void setTrueFasleMCQRadioButtonFirstTime(int trueFalseOrMCQId) {
        if(trueFalseOrMCQId == R.id.true_false_radio) {
            RadioButton tfRadio = findViewById(R.id.true_false_radio);
            tfRadio.setChecked(true);
        } else {
            RadioButton mcqRadio = findViewById(R.id.mcq_radio);
            mcqRadio.setChecked(true);
        }
    }

    private void setImageOrTextBasedRadioButtonFirstTime(int imageOrTextQuestionId) {
        if(imageOrTextQuestionId == R.id.text_radio_button) {
            RadioButton tRadio = findViewById(R.id.text_radio_button);
            tRadio.setChecked(true);
        } else {
            RadioButton iRadio = findViewById(R.id.image_radio_button);
            iRadio.setChecked(true);
        }
    }
    private void setupTextAndImage() {
        String questionID = question.getQuestionId();

        StorageReference questionImage = FileStorage.getProblemImageRef(Uri.parse(questionID + ".png"));
        final StorageReference questionText = FileStorage.getProblemImageRef(Uri.parse(questionID + ".txt"));
        try {
            final File tempImage = File.createTempFile(questionID, "png");
            final File tempText = File.createTempFile(questionID, "txt");
            setupImage(questionImage, tempImage);
            setupText(questionText, tempText);
        } catch (IOException e) {
            Toast.makeText(this, "An error occured when downloading the question", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupImage(StorageReference questionImage, final File tempImage){
        questionImage.getFile(tempImage).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap displayImage = BitmapFactory.decodeFile(tempImage.getAbsolutePath());
                ImageView displayImageView = findViewById(R.id.addQuestion_display_image);
                displayImageView.setImageBitmap(displayImage);

                TextView noImageSelected = findViewById(R.id.display_question_path);
                noImageSelected.setVisibility(View.GONE);

                setImageOrTextBasedRadioButtonFirstTime(R.id.image_radio_button);
                setsUpImageOrTextBasedRadioButtons(R.id.image_radio_button);

                ProgressBar progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(View.GONE);

                bitmap = displayImage;
            }
        });
    }

    private void setupText(StorageReference questionText, final File tempText){
        questionText.getFile(tempText).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                String displayText = "";
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(tempText.getAbsolutePath())));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }

                    reader.close();

                    if (sb.length() > 0) {
                        displayText = sb.toString().substring(0, sb.length() - 1);
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                    finish();
                }
                EditText questionEditText = findViewById(R.id.questionText);
                questionEditText.setText(displayText);
                setImageOrTextBasedRadioButtonFirstTime(R.id.text_radio_button);
                setsUpImageOrTextBasedRadioButtons(R.id.text_radio_button);

                ProgressBar progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
