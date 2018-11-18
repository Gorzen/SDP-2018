package ch.epfl.sweng.studyup.questions;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.FileStorage;
import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.player.Player;

import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.RefreshContext;
import ch.epfl.sweng.studyup.utils.navigation.NavigationStudent;
import ch.epfl.sweng.studyup.player.QuestsActivityStudent;
import ch.epfl.sweng.studyup.utils.navigation.NavigationStudent;

public class DisplayQuestionActivity extends NavigationStudent {

    @SuppressWarnings("HardCodedStringLiteral")
    private final String TAG = "DisplayQuestionActivity";
    @SuppressWarnings("HardCodedStringLiteral")
    public static final String DISPLAY_QUESTION_TITLE = "display_question_title";
    @SuppressWarnings("HardCodedStringLiteral")
    public static final String DISPLAY_QUESTION_ID = "display_question_id";
    @SuppressWarnings("HardCodedStringLiteral")
    public static final String DISPLAY_QUESTION_TRUE_FALSE = "display_question_true_false";
    @SuppressWarnings("HardCodedStringLiteral")
    public static final String DISPLAY_QUESTION_ANSWER = "display_question_answer";
    public static final int XP_GAINED_WITH_QUESTION = 10;
    private Question displayQuestion;

    private RadioGroup answerGroupTOP;
    private RadioGroup answerGroupBOT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_question);

        int answerNumber = 0;
        boolean trueFalse = false;
        String questionTitle = "";
        String questionID = "";

        Intent intent = getIntent();
        if(!checkIntent(intent)) return;

        questionTitle = intent.getStringExtra(DISPLAY_QUESTION_TITLE);
        questionID = intent.getStringExtra(DISPLAY_QUESTION_ID);
        answerNumber = Integer.parseInt(intent.getStringExtra(DISPLAY_QUESTION_ANSWER));
        trueFalse = Boolean.parseBoolean(intent.getStringExtra(DISPLAY_QUESTION_TRUE_FALSE));


        //Create the question
        displayQuestion = new Question(questionID, questionTitle, trueFalse, answerNumber, Constants.Course.SWENG.name()); //TODO put basic course, consistent? (We don't need the course in this activity so no need to put it in intent)
        displayImage(questionID);
        setupLayout(displayQuestion);


        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setupRadioButton();

        TextView questTitle = findViewById(R.id.quest_title);
        questTitle.setText(displayQuestion.getTitle());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
    }

    /**
     * Check that the Intent that launched the activity has all the needed fields
     *
     * @return true iff the intent contains the needed fields
     */
    private boolean checkIntent(Intent intent) {

        if (!intent.hasExtra(DISPLAY_QUESTION_TITLE)) {
            quit();
            return false;
        }

        if (!intent.hasExtra(DISPLAY_QUESTION_ID)) {
            quit();
            return false;
        }

        if (!intent.hasExtra(DISPLAY_QUESTION_ANSWER)) {
            quit();
            return false;
        }

        if (!intent.hasExtra(DISPLAY_QUESTION_TRUE_FALSE)) {
            quit();
            return false;
        }

        return true;
    }

    private void setupRadioButton() {
        answerGroupTOP = findViewById(R.id.answer_radio_group_top);
        answerGroupBOT = findViewById(R.id.answer_radio_group_bot);
        answerGroupTOP.clearCheck();
        answerGroupBOT.clearCheck();
        answerGroupTOP.setOnCheckedChangeListener(listener1);
        answerGroupBOT.setOnCheckedChangeListener(listener2);

        List<RadioButton> radioButtons = new ArrayList<>(Arrays.asList(
                (RadioButton) findViewById(R.id.answer1),
                (RadioButton) findViewById(R.id.answer2),
                (RadioButton) findViewById(R.id.answer3),
                (RadioButton) findViewById(R.id.answer4)));

        for (RadioButton rdb : radioButtons) {
            rdb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(buttonView.isChecked()) {
                        buttonView.setBackgroundResource(R.drawable.button_quests_clicked_shape);
                    }
                    else buttonView.setBackgroundResource(R.drawable.button_quests_shape);
                }
            });
        }

    }

    /**
     * Listeners that allows us to have two columns of radio buttons, without two buttons checkable
     * */
    private RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            setListener(checkedId, answerGroupBOT, listener2);
        }
    };
    private RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            setListener(checkedId, answerGroupTOP, listener1);
        }
    };
    private void setListener(int checkedId, RadioGroup answerGroup, RadioGroup.OnCheckedChangeListener listener) {
        if (checkedId != -1) {
            answerGroup.setOnCheckedChangeListener(null);
            answerGroup.clearCheck();
            answerGroup.setOnCheckedChangeListener(listener);
        }
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
            Toast.makeText(this, getString(R.string.text_questiondlerror), Toast.LENGTH_SHORT).show();
            quit();
        }
    }

    private void quit() {
        Toast.makeText(this, getString(R.string.text_questiondisplayerror), Toast.LENGTH_SHORT);
        Log.e(TAG, "Bad intent given in parameters");
        super.onBackPressed();
    }

    private void setupLayout(Question question){
        if (!question.isTrueFalse()){
            TextView answer1 = findViewById(R.id.answer1);
            answer1.setText(getString(R.string.text_answer_1));

            TextView answer2 = findViewById(R.id.answer2);
            answer2.setText(getString(R.string.text_answer_2));

            TextView answer3 = findViewById(R.id.answer3);
            answer3.setVisibility(View.VISIBLE);

            TextView answer4 = findViewById(R.id.answer4);
            answer4.setVisibility(View.VISIBLE);
        }
    }


    public void answerQuestion(View view) {
        int chkTOP = answerGroupTOP.getCheckedRadioButtonId();
        int chkBOT = answerGroupBOT.getCheckedRadioButtonId();
        if(chkBOT == -1 && chkTOP==-1) {
            Toast.makeText(this, getString(R.string.text_makechoice), Toast.LENGTH_SHORT).show();
        }
        else {
            int realCheck = (chkTOP == -1) ? chkBOT : chkTOP;
            RadioButton checkedAnswer = findViewById(realCheck);

            //subtract 1 to have answer between 0 and 3
            int answer = Integer.parseInt(checkedAnswer.getTag().toString()) - 1;

            //TODO : What to do next ?
            if(Player.get().getAnsweredQuestion().containsKey(displayQuestion.getQuestionId())) {
                Toast.makeText(this, getString(R.string.text_cantanswertwice), Toast.LENGTH_SHORT).show();
            }

            else if (answer == displayQuestion.getAnswer()) {
                goodAnswer();
            } else {
                badAnswer();
            }

            Intent goToQuests = new Intent(this, QuestsActivityStudent.class);
            startActivity(goToQuests);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    private void badAnswer() {
        Player.get().addAnsweredQuestion(displayQuestion.getQuestionId(), false);
        Toast.makeText(this, getString(R.string.text_wronganswer), Toast.LENGTH_SHORT).show();
    }

    private void goodAnswer() {
        Player.get().addAnsweredQuestion(displayQuestion.getQuestionId(), true);
        Toast.makeText(this, getString(R.string.text_correctanswer), Toast.LENGTH_SHORT).show();
        Player.get().addExperience(XP_GAINED_WITH_QUESTION, this);

        //Randomly add one item to the player
        Random random = new Random();
        boolean rng = random.nextBoolean();
        if(rng){
            Player.get().addItem(Items.XP_POTION);
        }else{
            Player.get().addItem(Items.COIN_SACK);
        }
    }


    /**
     * @param c The context of the application that launch the intent (put this)
     * @param q The question that needs to be passed
     * @return The intent ready to be launched with "startActivity"
     */
    public static Intent getIntentForDisplayQuestion(Context c, Question q) {
        Intent goToQuestion = new Intent(c, DisplayQuestionActivity.class);
        goToQuestion.putExtra(DISPLAY_QUESTION_TITLE, q.getTitle());
        goToQuestion.putExtra(DISPLAY_QUESTION_ID, q.getQuestionId());
        goToQuestion.putExtra(DISPLAY_QUESTION_TRUE_FALSE, Boolean.toString(q.isTrueFalse()));
        goToQuestion.putExtra(DISPLAY_QUESTION_ANSWER, Integer.toString(q.getAnswer()));
        return goToQuestion;
    }
}
