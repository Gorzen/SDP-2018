package ch.epfl.sweng.studyup.questions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.widget.Toolbar;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.FileStorage;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.teacher.QuestsActivityTeacher;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.Constants.Course;
import ch.epfl.sweng.studyup.utils.imagePathGetter.imagePathGetter;
import ch.epfl.sweng.studyup.utils.imagePathGetter.mockImagePathGetter;
import ch.epfl.sweng.studyup.utils.imagePathGetter.pathFromGalleryGetter;
import ch.epfl.sweng.studyup.utils.navigation.NavigationStudent;

import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_UUID;
import static ch.epfl.sweng.studyup.utils.Utils.getStringListFromCourseList;

@SuppressWarnings("HardCodedStringLiteral")
public class AddOrEditQuestionActivity extends NavigationStudent {
    private static final String TAG = "AddOrEditQuestionAct";

    private static final int READ_REQUEST_CODE = 42;
    private Uri imageURI = null;

    private RadioGroup trueFalseRadioGroup, imageTextRadioGroup, langRadioGroup;
    private imagePathGetter getPath;
    private Course chosenCourse;
    private TextView view_chosen_course;
    private Bitmap bitmap = null;
    private boolean isNewQuestion = true;
    private Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        Intent intent = getIntent();
        Question question = (Question) intent.getSerializableExtra(AddOrEditQuestionActivity.class.getSimpleName());

        view_chosen_course = findViewById(R.id.chosenCourseTextView);

        // Setup progress bar
        ProgressBar progressBar = findViewById(R.id.progressBar);
        if(MOCK_ENABLED) progressBar.setVisibility(View.GONE); else progressBar.setVisibility(View.VISIBLE);

        // Setup path getter
        if (MOCK_ENABLED) {
            getPath = new mockImagePathGetter(this, READ_REQUEST_CODE);
        } else {
            Firestore.get().loadQuestions(this);
            getPath = new pathFromGalleryGetter(this, READ_REQUEST_CODE);
        }

        this.question = question;
        isNewQuestion = false;
        int trueFalseOrMCQ = question.isTrueFalse() ? R.id.true_false_radio : R.id.mcq_radio;
        int langButtonId = question.getLang().equals("en") ? R.id.radio_en : R.id.radio_fr;
        chosenCourse = Course.valueOf(question.getCourseName());
        setupEditQuestion(trueFalseOrMCQ, langButtonId);

        addRadioListener();
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
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
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
                            //TextView displayName = findViewById(R.id.display_question_path);
                            //displayName.setVisibility(View.GONE);
                        } catch (IOException e) {
                            Log.e(TAG, "An error occurred when displaying the image");
                        }
                        /*TextView displayName = findViewById(R.id.display_question_path);
                        displayName.setText(imageURI.toString());*/
                    }
                });
            }
        }
    }


    public void addQuestion(View current) {
        if (imageURI != null || bitmap != null || imageTextRadioGroup.getCheckedRadioButtonId() == R.id.text_radio_button) {
            RadioGroup answerGroup = findViewById(R.id.question_radio_group);
            RadioButton checkedButton = findViewById(answerGroup.getCheckedRadioButtonId());
            //get the tag of the button to know the answer number
            int answerNumber = Integer.parseInt(checkedButton.getTag().toString()) - 1;


            boolean isTrueFalseQuestion = trueFalseRadioGroup.getCheckedRadioButtonId() == R.id.true_false_radio;

            langRadioGroup = findViewById(R.id.lang_radio_group);
            String langQuestion = langRadioGroup.getCheckedRadioButtonId() == R.id.radio_en ? "en" : "fr";
            String newQuestionID = isNewQuestion ? getUUID() : question.getQuestionId();

            EditText newQuestionTitleView = findViewById(R.id.questionTitle);
            String newQuestionTitle = newQuestionTitleView.getText().toString();

            RadioGroup imageTextRadioGroup = findViewById(R.id.text_or_image_radio_group);
            File questionFile;

            if (imageTextRadioGroup.getCheckedRadioButtonId() == R.id.image_radio_button) {
                //Delete the txt file, if there was any
                FileStorage.getProblemImageRef(Uri.parse(newQuestionID + ".txt")).delete();
                questionFile = setupFileImage(newQuestionID);
            } else {
                //If the edited question goes from image to text, we delete the image from firebase
                FileStorage.getProblemImageRef(Uri.parse(newQuestionID + ".png")).delete();
                questionFile = setupFileText(newQuestionID);
            }

            Log.e(TAG, "create the question");
            if (newQuestionTitle.length() == 0) {
                Toast.makeText(this.getApplicationContext(), getString(R.string.text_insert_title_for_question), Toast.LENGTH_SHORT).show();
                return;
            }

            if(chosenCourse==null){
                Toast.makeText(this.getApplicationContext(), getString(R.string.text_select_course_for_question), Toast.LENGTH_SHORT).show();
                return;
            }
            String questionCourseName = chosenCourse.name();

            Question newQuestion = new Question(newQuestionID, newQuestionTitle, isTrueFalseQuestion, answerNumber, questionCourseName, langQuestion);

            // Upload the problem image file to the Firebase Storage server
            FileStorage.uploadProblemImage(questionFile);
            // Add question to FireStore
            Firestore.get().addQuestion(newQuestion);

            if(isNewQuestion) {
                Toast.makeText(this.getApplicationContext(), getString(R.string.question_added), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this.getApplicationContext(), getString(R.string.question_edited), Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

    private File setupFileImage(String ID) {
        File questionFile = new File(this.getApplicationContext().getFilesDir(), ID + ".png");
        try {
            Bitmap imageBitmap = imageURI == null ? bitmap : getBitmapFromUri(imageURI);
            FileOutputStream out = new FileOutputStream(questionFile);
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        return questionFile;
    }

    private File setupFileText(String ID) {
        try {
            Log.e(TAG, "text selected write file");
            File questionFile = new File(this.getApplicationContext().getFilesDir(), ID + ".txt");
            FileWriter writer = new FileWriter(questionFile);
            TextView questionTextView = findViewById(R.id.questionText);
            String questionData = questionTextView.getText().toString();
            if (questionData.isEmpty()) {
                Toast.makeText(this.getApplicationContext(), getString(R.string.text_insert_title_for_question), Toast.LENGTH_SHORT).show();
                return null;
            }
            writer.write(questionData);
            writer.close();

            return questionFile;
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

        return null;
    }

    private String getUUID() {
        if (MOCK_ENABLED) {
            return MOCK_UUID;
        } else {
            return UUID.randomUUID().toString();
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
                setUpImageOrTextBasedRadioButtons(checkedId);
            }
        });

        langRadioGroup = findViewById(R.id.lang_radio_group);
    }

    /**
     * Sets the MCQ or True/False Radio Buttons. This method is used when a question is being edited
     * to display the corresponding checked radio buttons and is also used when the radio listener is being set
     *
     * @param trueFalseOrMCQID chooses between MCQ or True/False alternatives
     */
    public void setUpMCQTrueFalseRadioButtons(int trueFalseOrMCQID) {
        RadioButton firstRadioButton = findViewById(R.id.radio_answer1);
        RadioButton secondRadioButton = findViewById(R.id.radio_answer2);
        RadioButton thirdRadioButton = findViewById(R.id.radio_answer3);
        RadioButton fourthRadioButton = findViewById(R.id.radio_answer4);

        if (trueFalseOrMCQID == R.id.true_false_radio) {
            setupRadioButtonForTrueFalse(firstRadioButton, secondRadioButton, thirdRadioButton, fourthRadioButton);
        } else {
            setupRadioButtonForMCQ(firstRadioButton, secondRadioButton, thirdRadioButton, fourthRadioButton);
        }
    }

    private void setupRadioButtonForTrueFalse(RadioButton r1, RadioButton r2, RadioButton r3, RadioButton r4) {
        //mask the 3rd and 4th radio button and uncheck them
        r3.setVisibility(View.INVISIBLE);
        r3.setChecked(false);
        r4.setVisibility(View.INVISIBLE);
        r4.setChecked(false);
        //Change the text to the 1st and second button to True and False
        r1.setText(getString(R.string.truth_value));
        r2.setText(getString(R.string.false_value));
        if (!isNewQuestion && question.isTrueFalse()) {
            switch (question.getAnswer()) {
                case 0:
                    r1.setChecked(true);
                    break;
                case 1:
                    r2.setChecked(true);
                    break;
            }
        }  else {
            r1.setChecked(true);
        }
    }

    private void setupRadioButtonForMCQ(RadioButton r1, RadioButton r2, RadioButton r3, RadioButton r4) {
        //unmask the last two buttons and set the text to the first ones to numbers
        r3.setVisibility(View.VISIBLE);
        r3.setChecked(false);
        r4.setVisibility(View.VISIBLE);
        r4.setChecked(false);
        if (!isNewQuestion && !question.isTrueFalse()) {
            switch (question.getAnswer()) {
                case 0:
                    r1.setChecked(true);
                    break;
                case 1:
                    r2.setChecked(true);
                    break;
                case 2:
                    r3.setChecked(true);
                    break;
                case 3:
                    r4.setChecked(true);
                    break;
            }
        } else {
            r1.setChecked(true);
        }
        //Change the text to the 1st and second button to True and False
        r1.setText("1");
        r2.setText("2");
    }
    /**
     * Sets the Image-based or Text-based Radio Buttons. This method is used when a question is being edited
     * to display the corresponding checked radio buttons and is also used when the radio listener is being set
     *
     * @param imageOrTextQuestioniD chooses between image-based or text-based alternatives
     */
    private void setUpImageOrTextBasedRadioButtons(int imageOrTextQuestioniD) {
        Button selectImageButton = findViewById(R.id.selectImageButton);
        ImageView imageQuestion = findViewById(R.id.addQuestion_display_image);
        TextView questionText = findViewById(R.id.questionText);
        if (imageOrTextQuestioniD == R.id.text_radio_button) {
            //mask everything related to the image-based question
            selectImageButton.setVisibility(View.GONE);
            imageQuestion.setVisibility(View.GONE);
            //display the text for the question
            questionText.setVisibility(View.VISIBLE);
        } else {
            //show everything related to the image-based question
            selectImageButton.setVisibility(View.VISIBLE);
            imageQuestion.setVisibility(View.VISIBLE);
            //mask the text for the question
            questionText.setVisibility(View.GONE);
        }
    }

    private void setupEditQuestion(int trueFalseOrMCQId, int langButtonId) {
        changeAddButtonToEditButton();
        setUpQuestionTitle();
        setTrueFasleMCQRadioButtonFirstTime(trueFalseOrMCQId);
        setUpMCQTrueFalseRadioButtons(trueFalseOrMCQId);
        setupTextAndImage();
        setupLang(langButtonId);

        view_chosen_course.setText(getString(R.string.chosen_course_for_question)+ chosenCourse.toString());
    }

    private void changeAddButtonToEditButton() {
        Button addEditButton = findViewById(R.id.addOrEditQuestionButton);
        addEditButton.setText(getString(R.string.edit_button_text));
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public void onClickCourseChoice(View view) {
        AlertDialog.Builder courseChoiceBuilder = new AlertDialog.Builder(this);
        courseChoiceBuilder.setTitle(getString(R.string.course_for_this_quest));

        final List<Course> courses = Player.get().getRole() == Constants.Role.teacher ?
                Player.get().getCoursesTeached() : Player.get().getCoursesEnrolled();
        ArrayList<String> stringList = getStringListFromCourseList(courses, true);
        String[] stringArray = new String[stringList.size()];
        courseChoiceBuilder.setItems(stringList.toArray(stringArray), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (Course c : courses){
                    if(which == c.ordinal()){
                        chosenCourse = c;
                        view_chosen_course.setText(getString(R.string.chosen_course_for_question)+c.toString());
                    }
                }
            }
        });
        courseChoiceBuilder.setNegativeButton(getString(R.string.cancel), null);
        courseChoiceBuilder.create().show();
    }


    private void setUpQuestionTitle() {
        TextView questionTitle = findViewById(R.id.questionTitle);
        questionTitle.setText(question.getTitle());
    }


    private void setupLang(int langButtonId) {
        RadioButton langSelected = findViewById(langButtonId);
        langSelected.setChecked(true);
    }

    private void setTrueFasleMCQRadioButtonFirstTime(int trueFalseOrMCQId) {
        if (trueFalseOrMCQId == R.id.true_false_radio) {
            RadioButton tfRadio = findViewById(R.id.true_false_radio);
            tfRadio.setChecked(true);
        } else {
            RadioButton mcqRadio = findViewById(R.id.mcq_radio);
            mcqRadio.setChecked(true);
        }
    }

    private void setImageOrTextBasedRadioButtonFirstTime(int imageOrTextQuestionId) {
        if (imageOrTextQuestionId == R.id.text_radio_button) {
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
            Toast.makeText(this, getString(R.string.error_when_download_question), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupImage(StorageReference questionImage, final File tempImage) {
        questionImage.getFile(tempImage).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap displayImage = BitmapFactory.decodeFile(tempImage.getAbsolutePath());
                ImageView displayImageView = findViewById(R.id.addQuestion_display_image);
                displayImageView.setImageBitmap(displayImage);

                setImageOrTextBasedRadioButtonFirstTime(R.id.image_radio_button);
                setUpImageOrTextBasedRadioButtons(R.id.image_radio_button);

                ProgressBar progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(View.GONE);

                bitmap = displayImage;
            }
        });
    }

    public void setupText(StorageReference questionText, final File tempText) {
        questionText.getFile(tempText).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                String displayText = "";
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(tempText.getAbsolutePath())));
                    StringBuilder sb = new StringBuilder(); String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }

                    reader.close();

                    if (sb.length() > 0) {
                        displayText = sb.toString().substring(0, sb.length() - 1);
                    }
                } catch (Exception e) { Log.e(TAG, e.toString()); finish(); }

                ((EditText) findViewById(R.id.questionText)).setText(displayText);
                setImageOrTextBasedRadioButtonFirstTime(R.id.text_radio_button);
                setUpImageOrTextBasedRadioButtons(R.id.text_radio_button);

                (findViewById(R.id.progressBar)).setVisibility(View.GONE);
            }
        });
    }

    public void onBackButtonAddQuestion(View view) {
        startActivity(new Intent(this.getApplicationContext(), QuestsActivityTeacher.class));
    }

}
