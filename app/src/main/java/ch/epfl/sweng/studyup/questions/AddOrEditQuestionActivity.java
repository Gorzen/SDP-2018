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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
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
import static ch.epfl.sweng.studyup.utils.Utils.setupToolbar;

@SuppressWarnings("HardCodedStringLiteral")
public class AddOrEditQuestionActivity extends NavigationStudent {
    private static final String TAG = "AddOrEditQuestionAct";

    private static final int READ_REQUEST_CODE = 42;
    private Uri imageURI = null;

    private RadioGroup langRadioGroup;
    private imagePathGetter getPath;
    private Course chosenCourse;
    public long chosenDuration;
    private TextView view_chosen_duration;
    private Bitmap bitmap = null;
    private boolean isNewQuestion = true;
    private Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_question);
        setupToolbar(this);

        Intent intent = getIntent();
        Question question = (Question) intent.getSerializableExtra(AddOrEditQuestionActivity.class.getSimpleName());

        view_chosen_duration = findViewById(R.id.chosen_duration_text);
        view_chosen_duration.setText(getString(R.string.chosen_duration) + getString(R.string.time_constraint_text));
        //No time limit by default
        chosenDuration = 0;

        // Setup path getter
        if (MOCK_ENABLED) {
            getPath = new mockImagePathGetter(this, READ_REQUEST_CODE);
        } else {
            Firestore.get().loadQuestions(this);
            getPath = new pathFromGalleryGetter(this, READ_REQUEST_CODE);
        }

        if(question != null) {
            if(!MOCK_ENABLED) findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            this.question = question;
            isNewQuestion = false;
            int trueFalseOrMCQ = question.isTrueFalse() ? R.id.true_false_radio : R.id.mcq_radio;
            int langButtonId = question.getLang().equals("en") ? R.id.radio_en : R.id.radio_fr;
            chosenCourse = Course.valueOf(question.getCourseName());
            AddOrEditQuestionActivityHelperMethods.setupEditQuestion(this, trueFalseOrMCQ, langButtonId, question.getTitle());
        }

        AddOrEditQuestionActivityHelperMethods.addRadioListener(this);
    }

    /**
     * Methods present to make the refactor of the class possible, used to retrieve the state of the question
     * or to set fields.
     */
    protected boolean isNewQuestion() {
        return isNewQuestion;
    }
    protected boolean isTrueFalse() {
        return question.isTrueFalse();
    }
    protected int getAnswer() {
        return question.getAnswer();
    }
    protected String getId() {
        return question.getQuestionId();
    }
    protected void setBitmap(Bitmap b) {
        bitmap = b;
    }
    protected Course getChosenCourse() {
        return chosenCourse;
    }
    /**
     * Function called when the user wants to choose an image in gallery    
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
        RadioGroup imageTextRadioGroup = findViewById(R.id.text_or_image_radio_group), trueFalseRadioGroup = findViewById(R.id.true_false_or_mcq_radio_group);
        if (!(imageURI != null || bitmap != null || imageTextRadioGroup.getCheckedRadioButtonId() == R.id.text_radio_button)) return;
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

        Question newQuestion = new Question(newQuestionID, newQuestionTitle, isTrueFalseQuestion, answerNumber, questionCourseName, langQuestion, chosenDuration);

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

        final List<Course> courses = Player.get().isTeacher() ?
                Player.get().getCoursesTeached() : Player.get().getCoursesEnrolled();
        ArrayList<String> stringList = getStringListFromCourseList(courses, true);
        final ArrayList<String> stringListName = getStringListFromCourseList(courses, false);
        final String[] coursesArray = stringList.toArray(new String[stringList.size()]);
        courseChoiceBuilder.setItems(coursesArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*
                for (Course c : courses){
                    if(which == c.ordinal()-1){
                        chosenCourse = c;
                        view_chosen_course.setText(getString(R.string.chosen_course_for_question)+c.toString());
                    }
                }*/

                chosenCourse = Course.valueOf(stringListName.get(which));
                ((TextView) findViewById(R.id.chosenCourseTextView)).setText(getString(R.string.chosen_course_for_question)+chosenCourse.toString());
            }
        });
        courseChoiceBuilder.setNegativeButton(getString(R.string.cancel), null);
        courseChoiceBuilder.create().show();
    }

    public void onClickDurationChoice(View view) {
        AlertDialog.Builder durationChoiceBuilder = new AlertDialog.Builder(this);
        durationChoiceBuilder.setTitle(R.string.duration);

        ArrayList<String> durationChoice = new ArrayList<>(Constants.durationChoice);
        String[] durationChoiceArray = durationChoice.toArray(new String[durationChoice.size()]);
        durationChoiceBuilder.setItems(durationChoiceArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chosenDuration = Constants.durationCorrespond.get(which);
                if (chosenDuration == 0) {
                    view_chosen_duration.setText(getString(R.string.chosen_duration) + getString(R.string.time_constraint_text));
                } else {
                    view_chosen_duration.setText(getString(R.string.chosen_duration) + Constants.durationChoice.get(which));
                }
            }
        });
        durationChoiceBuilder.setNegativeButton(getString(R.string.cancel), null);
        durationChoiceBuilder.create().show();
    }

    public void onBackButtonAddQuestion(View view) {
        startActivity(new Intent(this.getApplicationContext(), QuestsActivityTeacher.class));
    }

}
