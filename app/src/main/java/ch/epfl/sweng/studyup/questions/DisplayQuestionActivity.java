package ch.epfl.sweng.studyup.questions;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import ch.epfl.sweng.studyup.MainActivity;
import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.FileStorage;
import ch.epfl.sweng.studyup.player.Player;

public class DisplayQuestionActivity extends AppCompatActivity {

    private final String TAG = "DisplayQuestionActivity";
    public static final String DISPLAY_QUESTION_TITLE = "display_question_title";
    public static final String DISPLAY_QUESTION_ID = "display_question_id";
    public static final String DISPLAY_QUESTION_TRUE_FALSE = "display_question_true_false";
    public static final String DISPLAY_QUESTION_ANSWER = "display_question_answer";
    public static final int XP_GAINED_WITH_QUESTION = 10;

    private Question displayQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_question);

        int answerNumber = 0;
        boolean trueFalse = false;
        String questionTitle = "";
        String questionID = "";

        Intent intent =  getIntent();
        //Get the Uri from the intent
        if (!intent.hasExtra(DISPLAY_QUESTION_TITLE)) {
            quit();
            return;
        }else
            questionTitle = intent.getStringExtra(DISPLAY_QUESTION_TITLE);

        //Get the question ID
        if (!intent.hasExtra(DISPLAY_QUESTION_ID)) {
            quit();
            return;
        }else
            questionID = intent.getStringExtra(DISPLAY_QUESTION_ID);

        //Get the answer
        if (!intent.hasExtra(DISPLAY_QUESTION_ANSWER)) {
            quit();
            return;
        }else
            answerNumber = Integer.parseInt(intent.getStringExtra(DISPLAY_QUESTION_ANSWER));

        //Now the boolean isTrueFale
        if (!intent.hasExtra(DISPLAY_QUESTION_TRUE_FALSE)) {
            quit();
            return;
        }else
            trueFalse = Boolean.parseBoolean(intent.getStringExtra(DISPLAY_QUESTION_TRUE_FALSE));

        //Create the question
        displayQuestion = new Question(questionID, questionTitle, trueFalse, answerNumber);
        displayImage(questionID);
        setupLayout(displayQuestion);

        Button backButton = (Button) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void displayImage(String questionID){
        StorageReference questionImage = FileStorage.getProblemImageRef(Uri.parse(questionID + ".png"));
        try {
            final File tempImage = File.createTempFile(questionID, "png");
            questionImage.getFile(tempImage).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    ProgressBar progressBar = findViewById(R.id.questionProgressBar);
                    progressBar.setVisibility(View.GONE);
                    Bitmap displayImage = BitmapFactory.decodeFile(tempImage.getAbsolutePath());
                    ImageView displayImageView = findViewById(R.id.question_display_view);
                    displayImageView.setImageBitmap(displayImage);
                }
            });
        }catch(IOException e){
            Toast.makeText(this, "An error occured when downloading the question", Toast.LENGTH_SHORT).show();
            quit();
        }
    }

    private void quit(){
        Toast.makeText(this, "Error while displaying the question", Toast.LENGTH_SHORT);
        Log.e(TAG, "Bad intent given in parameters");
        super.onBackPressed();
    }

    private void setupLayout(Question question){
        if (!question.isTrueFalse()){
            TextView answer1 = findViewById(R.id.answer1);
            answer1.setText("1");

            TextView answer2 = findViewById(R.id.answer2);
            answer2.setText("2");

            TextView answer3 = findViewById(R.id.answer3);
            answer3.setVisibility(View.VISIBLE);

            TextView answer4 = findViewById(R.id.answer4);
            answer4.setVisibility(View.VISIBLE);
        }
    }

    public void answerQuestion(View view){
        RadioGroup answerGroup = findViewById(R.id.answer_radio_group);
        RadioButton checkedAnswer = findViewById(answerGroup.getCheckedRadioButtonId());
        //subtract 1 to have answer between 0 and 3
        int answer = Integer.parseInt(checkedAnswer.getTag().toString()) - 1;

        //TODO : What to do next ?
        if (answer == displayQuestion.getAnswer()){
            Toast.makeText(this, "Correct answer ! Congrats", Toast.LENGTH_SHORT).show();
            Player.get().addExperience(XP_GAINED_WITH_QUESTION);
        }else{
            Toast.makeText(this, "Wrong answer... Maybe next time ?", Toast.LENGTH_SHORT).show();
        }

        Intent goToMain = new Intent(this, MainActivity.class);
        startActivity(goToMain);
    }

    /**
     *
     * @param c The context of the application that launch the intent (put this)
     * @param q The question that needs to be passed
     * @return The intent ready to be launched with "startActivity"
     */
    public static Intent getIntentForDisplayQuestion(Context c, Question q){
        Intent goToQuestion = new Intent(c, DisplayQuestionActivity.class);
        goToQuestion.putExtra(DISPLAY_QUESTION_TITLE, q.getTitle());
        goToQuestion.putExtra(DISPLAY_QUESTION_ID, q.getQuestionId());
        goToQuestion.putExtra(DISPLAY_QUESTION_TRUE_FALSE, Boolean.toString(q.isTrueFalse()));
        goToQuestion.putExtra(DISPLAY_QUESTION_ANSWER, Integer.toString(q.getAnswer()));
        return goToQuestion;
    }
}
