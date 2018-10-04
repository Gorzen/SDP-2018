package ch.epfl.sweng.studyup;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.UUID;

public class AddQuestionActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 42;
    private Uri imageURI = null;
    private RadioGroup trueFalseRadioGroup;
    private RadioButton checkedRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        addRadioListener();
    }

    public void performFileSearch(View current) {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened"
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        intent.setType("image/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    public void addQuestion(View current) {
        if (imageURI != null) {
            RadioGroup answerGroup = (RadioGroup) findViewById(R.id.question_radio_group);
            RadioButton checkedButton = (RadioButton) findViewById(answerGroup.getCheckedRadioButtonId());
            //get the tag of the button to know the answer number
            int answerNumber = Integer.parseInt(checkedButton.getTag().toString());

            boolean isTrueFalseQuestion = trueFalseRadioGroup.getCheckedRadioButtonId() == R.id.true_false_radio;

            //TODO: create the question, but first find a way to save them to the disk
            String newQuestionFileID = UUID.randomUUID().toString();
            File questionFile = new File(this.getApplicationContext().getFilesDir(), newQuestionFileID);
            try {
                copyFile(new File(imageURI.getPath()), questionFile);
            }catch (IOException e){
                //TODO: display a toast with an error
            }
        }

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
                //TODO: Display the name of the image on the textView
                TextView displayName = (TextView) findViewById(R.id.display_question_path);
                displayName.setText("Image at: " + imageURI.getPath());
            }
        }
    }

    private void addRadioListener(){
        trueFalseRadioGroup = (RadioGroup) findViewById(R.id.true_false_radio_group);
        trueFalseRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton firstRadioButton = findViewById(R.id.radio_answer1);
                RadioButton secondRadioButton = findViewById(R.id.radio_answer2);
                RadioButton thirdRadioButton = findViewById(R.id.radio_answer3);
                RadioButton fourthRadioButton = findViewById(R.id.radio_answer4);

                if (checkedId == R.id.true_false_radio){
                    //mask the 3rd and 4th radio button and uncheck them
                    thirdRadioButton.setVisibility(View.INVISIBLE);
                    thirdRadioButton.setChecked(false);
                    fourthRadioButton.setVisibility(View.INVISIBLE);
                    fourthRadioButton.setChecked(false);

                    //Change the text to the 1st and second button to True and False
                    firstRadioButton.setText("True");
                    secondRadioButton.setText("False");

                } else {
                    //unmask the last two buttons and set the text to the first ones to numbers
                    thirdRadioButton.setVisibility(View.VISIBLE);
                    thirdRadioButton.setChecked(false);
                    fourthRadioButton.setVisibility(View.VISIBLE);
                    fourthRadioButton.setChecked(false);

                    //Change the text to the 1st and second button to True and False
                    firstRadioButton.setText("1");
                    secondRadioButton.setText("2");
                }
            }
        });
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    //TODO: Create a local file with a copy of the image in case the file gets deleted
}
