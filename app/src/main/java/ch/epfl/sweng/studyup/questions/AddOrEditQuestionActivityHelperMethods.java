package ch.epfl.sweng.studyup.questions;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.FileStorage;

import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;

abstract class AddOrEditQuestionActivityHelperMethods {
    private static final String TAG = AddOrEditQuestionActivityHelperMethods.class.getSimpleName();

    protected static void setupEditQuestion(Activity act, int trueFalseOrMCQId, int langButtonId, String questionTitle) {
        changeAddButtonToEditButton(act);
        setUpQuestionTitle(act, questionTitle);
        setTrueFasleMCQRadioButtonFirstTime(act, trueFalseOrMCQId);
        setUpMCQTrueFalseRadioButtons(act, trueFalseOrMCQId);
        setupTextAndImage(act);
        setupLang(act, langButtonId);

        String chosenCourseText = act.getString(R.string.chosen_course_for_question) + ((AddOrEditQuestionActivity) act).getChosenCourse().toString();
        ((TextView) act.findViewById(R.id.chosenCourseTextView)).setText(chosenCourseText);
    }

    private static void setupLang(Activity act, int langButtonId) {
        RadioButton langSelected = act.findViewById(langButtonId);
        langSelected.setChecked(true);
    }

    private static void changeAddButtonToEditButton(Activity act) {
        Button addEditButton = act.findViewById(R.id.addOrEditQuestionButton);
        addEditButton.setText(act.getString(R.string.edit_button_text));
    }

    private static void setUpQuestionTitle(Activity act, String title) {
        TextView questionTitle = act.findViewById(R.id.questionTitle);
        questionTitle.setText(title);
    }

    private static void setTrueFasleMCQRadioButtonFirstTime(Activity act, int trueFalseOrMCQId) {
        if (trueFalseOrMCQId == R.id.true_false_radio) {
            RadioButton tfRadio = act.findViewById(R.id.true_false_radio);
            tfRadio.setChecked(true);
        } else {
            RadioButton mcqRadio = act.findViewById(R.id.mcq_radio);
            mcqRadio.setChecked(true);
        }
    }

    /**
     * Sets the MCQ or True/False Radio Buttons. This method is used when a question is being edited
     * to display the corresponding checked radio buttons and is also used when the radio listener is being set
     *
     * @param trueFalseOrMCQID chooses between MCQ or True/False alternatives
     */
    private static void setUpMCQTrueFalseRadioButtons(Activity act, int trueFalseOrMCQID) {
        RadioButton firstRadioButton = act.findViewById(R.id.radio_answer1);
        RadioButton secondRadioButton = act.findViewById(R.id.radio_answer2);
        RadioButton thirdRadioButton = act.findViewById(R.id.radio_answer3);
        RadioButton fourthRadioButton = act.findViewById(R.id.radio_answer4);

        if (trueFalseOrMCQID == R.id.true_false_radio) {
            setupRadioButtonForTrueFalse(act, firstRadioButton, secondRadioButton, thirdRadioButton, fourthRadioButton);
        } else {
            setupRadioButtonForMCQ(act, firstRadioButton, secondRadioButton, thirdRadioButton, fourthRadioButton);
        }
    }

    private static void setupRadioButtonForMCQ(Activity act, RadioButton r1, RadioButton r2, RadioButton r3, RadioButton r4) {
        //unmask the last two buttons and set the text to the first ones to numbers
        r3.setVisibility(View.VISIBLE);
        r3.setChecked(false);
        r4.setVisibility(View.VISIBLE);
        r4.setChecked(false);
        if (!((AddOrEditQuestionActivity) act).isNewQuestion() && !((AddOrEditQuestionActivity) act).isTrueFalse()) {
            switch (((AddOrEditQuestionActivity) act).getAnswer()) {
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

    private static void setupRadioButtonForTrueFalse(Activity act, RadioButton r1, RadioButton r2, RadioButton r3, RadioButton r4) {
        //mask the 3rd and 4th radio button and uncheck them
        r3.setVisibility(View.INVISIBLE);
        r3.setChecked(false);
        r4.setVisibility(View.INVISIBLE);
        r4.setChecked(false);
        //Change the text to the 1st and second button to True and False
        r1.setText(act.getString(R.string.truth_value));
        r2.setText(act.getString(R.string.false_value));
        if (!((AddOrEditQuestionActivity) act).isNewQuestion() && ((AddOrEditQuestionActivity) act).isTrueFalse()) {
            switch (((AddOrEditQuestionActivity) act).getAnswer()) {
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

    private static void setupTextAndImage(Activity act) {
        String questionID = ((AddOrEditQuestionActivity) act).getId();

        StorageReference questionImage = FileStorage.getProblemImageRef(Uri.parse(questionID + ".png"));
        final StorageReference questionText = FileStorage.getProblemImageRef(Uri.parse(questionID + ".txt"));
        try {
            final File tempImage = File.createTempFile(questionID, "png");
            final File tempText = File.createTempFile(questionID, "txt");
            setupImage(act, questionImage, tempImage);
            setupText(act, questionText, tempText);
        } catch (IOException e) {
            Toast.makeText(act, act.getString(R.string.error_when_download_question), Toast.LENGTH_SHORT).show();
            act.finish();
        }
    }

    private static void setupImage(final Activity act, StorageReference questionImage, final File tempImage) {
        questionImage.getFile(tempImage).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap displayImage = BitmapFactory.decodeFile(tempImage.getAbsolutePath());
                ImageView displayImageView = act.findViewById(R.id.addQuestion_display_image);
                displayImageView.setImageBitmap(displayImage);

                setImageOrTextBasedRadioButtonFirstTime(act, R.id.image_radio_button);
                setUpImageOrTextBasedRadioButtons(act, R.id.image_radio_button);
                if(!MOCK_ENABLED) act.findViewById(R.id.progressBar).setVisibility(View.GONE);
                ((AddOrEditQuestionActivity) act).setBitmap(displayImage);
            }
        });
    }

    private static void setupText(final Activity act, StorageReference questionText, final File tempText) {
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
                } catch (Exception e) { Log.e(TAG, e.toString()); act.finish(); }
                if(!MOCK_ENABLED) act.findViewById(R.id.progressBar).setVisibility(View.GONE);
                ((EditText) act.findViewById(R.id.questionText)).setText(displayText);
                setImageOrTextBasedRadioButtonFirstTime(act, R.id.text_radio_button);
                setUpImageOrTextBasedRadioButtons(act, R.id.text_radio_button);
            }
        });
    }

    protected static void addRadioListener(final Activity act) {
        RadioGroup trueFalseRadioGroup = act.findViewById(R.id.true_false_or_mcq_radio_group);
        trueFalseRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setUpMCQTrueFalseRadioButtons(act, checkedId);
            }
        });

        RadioGroup imageTextRadioGroup = act.findViewById(R.id.text_or_image_radio_group);
        imageTextRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setUpImageOrTextBasedRadioButtons(act, checkedId);
            }
        });
    }

    /**
     * Sets the Image-based or Text-based Radio Buttons. This method is used when a question is being edited
     * to display the corresponding checked radio buttons and is also used when the radio listener is being set
     *
     * @param imageOrTextQuestioniD chooses between image-based or text-based alternatives
     */
    private static void setUpImageOrTextBasedRadioButtons(Activity act, int imageOrTextQuestioniD) {
        Button selectImageButton = act.findViewById(R.id.selectImageButton);
        ImageView imageQuestion = act.findViewById(R.id.addQuestion_display_image);
        TextView questionText = act.findViewById(R.id.questionText);
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

    private static void setImageOrTextBasedRadioButtonFirstTime(Activity act, int imageOrTextQuestionId) {
        if (imageOrTextQuestionId == R.id.text_radio_button) {
            RadioButton tRadio = act.findViewById(R.id.text_radio_button);
            tRadio.setChecked(true);
        } else {
            RadioButton iRadio = act.findViewById(R.id.image_radio_button);
            iRadio.setChecked(true);
        }
    }
}
