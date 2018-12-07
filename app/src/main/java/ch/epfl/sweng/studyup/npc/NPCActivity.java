package ch.epfl.sweng.studyup.npc;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.RefreshContext;
import ch.epfl.sweng.studyup.utils.Utils;

public class NPCActivity extends RefreshContext {
    private final NPC npc = null;

    private final static int TIME_BETWEEN_MESSAGES = 1500;
    private final static int TIME_BETWEEN_CHARACTERS = 10;

    private final List<Integer> messages = new ArrayList<Integer>() {
        {
            add(R.string.NPC_interaction);
            add(R.string.NPC_interaction);
            add(R.string.NPC_interaction);
            add(R.string.NPC_interaction);
            add(R.string.NPC_interaction);
            add(R.string.NPC_interaction);
            add(R.string.NPC_interaction);
            add(R.string.NPC_interaction);
            add(R.string.NPC_interaction);
            add(R.string.NPC_interaction);
            add(R.string.NPC_interaction);
            add(R.string.NPC_interaction);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_npc);
        Intent intent = getIntent();
        String npcName = intent.getStringExtra(Constants.NPC_ACTIVITY_INTENT_NAME);
        NPC npc = Utils.getNPCfromName(npcName);
        ImageView imageView = findViewById(R.id.npc_image);
        imageView.setImageResource(npc.getImage());

        setupMessages();
        displayMessages();
    }

    public void onBackButtonNPC(View v) {
        finish();
    }


    public void displayMessages() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            private int index = 0;

            @Override
            public void run() {
                if (index == messages.size()) {
                    TextView yesNo = findViewById(R.id.yes_no_button_npc);
                    yesNo.setVisibility(View.VISIBLE);

                    scroll();
                } else {
                    final TextView message = findViewById(index);
                    final CharSequence m = message.getText();

                    if (index % 2 == 1) {
                        Rect bounds = new Rect();
                        Paint textPaint = message.getPaint();
                        textPaint.getTextBounds(m.toString() + "M", 0, m.length() + 1, bounds);

                        message.setWidth(bounds.width() >= message.getMaxWidth() ?
                                message.getMaxWidth() : bounds.width());
                    }

                    message.setText("");
                    message.setVisibility(View.VISIBLE);

                    final Handler handlerText = new Handler();
                    handlerText.post(new Runnable() {
                        private int i = 1;

                        @Override
                        public void run() {
                            message.setText(m.subSequence(0, i));

                            scroll();

                            i++;
                            if (i <= m.length()) {
                                handlerText.postDelayed(this, TIME_BETWEEN_CHARACTERS);
                            }
                        }
                    });
                }

                index++;
                if (index <= messages.size()) {
                    handler.postDelayed(this, TIME_BETWEEN_MESSAGES);
                }
            }
        });
    }

    private void scroll(){
        final Handler scroll = new Handler();
        scroll.postDelayed(new Runnable() {
            @Override
            public void run() {
                ScrollView scrollNCP = findViewById(R.id.scrollView_npc);
                scrollNCP.fullScroll(View.FOCUS_DOWN);
            }
        }, TIME_BETWEEN_CHARACTERS);
    }

    public void setupMessages() {
        //List<Integer> messages = npc.getMessages();

        for (int i = 0; i < messages.size(); ++i) {
            String message = getString(messages.get(i));
            addMessage(message, i, messages.size() - 1);
        }

        fixYesNoMessage(messages.size() - 1);
    }

    private void addMessage(String m, int index, int maxIndex) {
        ConstraintLayout.LayoutParams lparams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        lparams.setMargins(0, 8, 0, 8);
        lparams.horizontalBias = index % 2;
        // If index = max
        lparams.bottomToTop = index == maxIndex ? R.id.yes_no_button_npc : index + 1;
        lparams.endToEnd = R.id.constraintLayout_npc;
        lparams.startToStart = R.id.constraintLayout_npc;
        // If index = 0
        lparams.topToBottom = index == 0 ? R.id.npc_image : index - 1;

        final TextView message = new TextView(this);
        message.setLayoutParams(lparams);
        message.setId(index);
        message.setText(m);
        message.setBackground(getDrawable(index % 2 == 0 ? R.drawable.message_from_npc : R.drawable.message_from_user));
        message.setTextColor(getResources().getColor(R.color.colorGreyBlack));
        message.setTextSize(20);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        message.setMaxWidth((int) (width * 0.7));

        message.setVisibility(View.GONE);

        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout_npc);
        constraintLayout.addView(message);
    }

    private void fixYesNoMessage(int maxIndex) {
        ConstraintLayout.LayoutParams lparams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        lparams.setMargins(8, 8, 8, 8);
        lparams.bottomToBottom = R.id.constraintLayout_npc;
        lparams.endToEnd = R.id.constraintLayout_npc;
        lparams.startToStart = R.id.constraintLayout_npc;
        lparams.topToBottom = maxIndex;

        TextView yesNo = findViewById(R.id.yes_no_button_npc);
        yesNo.setLayoutParams(lparams);
        yesNo.setVisibility(View.GONE);
    }
}
