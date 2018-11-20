package ch.epfl.sweng.studyup.questions;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import ch.epfl.sweng.studyup.utils.navigation.NavigationStudent;

public class DisplayQuestionMockActivity extends NavigationStudent {

    private final String TAG = "DisplayQuestionMockActivity";
    public static final int XP_GAINED_WITH_QUESTION = 10;
    private static final String mockUUID = "Fake UUID test Display";
    private final Question mockQuestion = new Question(mockUUID, "ADisplayQuestionActivityTest", false, 0, Constants.Course.SWENG.name());
    private Question displayQuestion;

    private RadioGroup answerGroupTOP;
    private RadioGroup answerGroupBOT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_question);

        displayQuestion = mockQuestion;
        displayImage(mockUUID);

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
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
                    if (buttonView.isChecked()) {
                        buttonView.setBackgroundResource(R.drawable.button_quests_clicked_shape);
                    } else buttonView.setBackgroundResource(R.drawable.button_quests_shape);
                }
            });
        }

    }

    /**
     * Listeners that allows us to have two columns of radio buttons, without two buttons checkable
     */
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


    private void displayImage(String questionID) {
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
        } catch (IOException e) {
            Toast.makeText(this, "An error occured when downloading the question", Toast.LENGTH_SHORT).show();
            quit();
        }
    }

    private void quit() {
        Toast.makeText(this, "Error while displaying the question", Toast.LENGTH_SHORT);
        Log.e(TAG, "Bad intent given in parameters");
        super.onBackPressed();
    }

    private void setupLayout(Question question) {
        if (!question.isTrueFalse()) {
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
        if (chkBOT == -1 && chkTOP == -1) {
            Toast.makeText(this, "Make your choice !", Toast.LENGTH_SHORT).show();
        } else {
            int realCheck = (chkTOP == -1) ? chkBOT : chkTOP;
            RadioButton checkedAnswer = findViewById(realCheck);

            //subtract 1 to have answer between 0 and 3
            int answer = Integer.parseInt(checkedAnswer.getTag().toString()) - 1;

            //TODO : What to do next ?
            if (Player.get().getAnsweredQuestion().containsKey(displayQuestion.getQuestionId())) {
                Toast.makeText(this, "You can't answer a question twice !", Toast.LENGTH_SHORT).show();
            } else if (answer == displayQuestion.getAnswer()) {
                goodAnswer();
            } else {
                badAnswer();
            }
        }
    }

    private void badAnswer() {
        Player.get().addAnsweredQuestion(displayQuestion.getQuestionId(), false);
        Toast.makeText(this, "Wrong answer... Maybe next time ?", Toast.LENGTH_SHORT).show();
    }

    private void goodAnswer() {
        Player.get().addAnsweredQuestion(displayQuestion.getQuestionId(), true);
        Toast.makeText(this, "Correct answer ! Congrats", Toast.LENGTH_SHORT).show();
        Player.get().addExperience(XP_GAINED_WITH_QUESTION, this);

        //Randomly add one item to the player
        Random random = new Random();
        boolean rng = random.nextBoolean();
        if (rng) {
            Player.get().addItem(Items.XP_POTION);
        } else {
            Player.get().addItem(Items.COIN_SACK);
        }
    }
}
