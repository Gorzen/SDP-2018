package ch.epfl.sweng.studyup.questions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import ch.epfl.sweng.studyup.MainActivity;
import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.Player;

public class DisplayQuestionActivity extends AppCompatActivity {

    private final String TAG = "DisplayQuestionActivity";
    public static final String DISPLAY_QUESTION_URI = "display_question_uri";
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
        Uri questionUri = Uri.EMPTY;

        Intent intent =  getIntent();
        //Get the Uri from the intent
        if (!intent.hasExtra(DISPLAY_QUESTION_URI)) {
            quit();
            return;
        }else
            questionUri = Uri.parse(intent.getStringExtra(DISPLAY_QUESTION_URI));

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
        displayQuestion = new Question(questionUri, trueFalse, answerNumber);
        displayImage(questionUri);
        setupLayout(displayQuestion);

    }

    private void displayImage(Uri uri){
        ImageView imageView = findViewById(R.id.question_display_view);
        imageView.setImageURI(uri);
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
        goToQuestion.putExtra(DISPLAY_QUESTION_URI, q.getQuestionUri().toString());
        goToQuestion.putExtra(DISPLAY_QUESTION_TRUE_FALSE, Boolean.toString(q.isTrueFalse()));
        goToQuestion.putExtra(DISPLAY_QUESTION_ANSWER, Integer.toString(q.getAnswer()));
        return goToQuestion;
    }
}
