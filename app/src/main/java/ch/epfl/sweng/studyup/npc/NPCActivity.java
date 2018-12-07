package ch.epfl.sweng.studyup.npc;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.RefreshContext;
import ch.epfl.sweng.studyup.utils.Utils;

public class NPCActivity extends RefreshContext {
    private final NPC npc = null;
    private final List<Integer> messages = new ArrayList<Integer>(){
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
    }

    public void onBackButtonNPC(View v) {
        finish();
    }

    public void setupMessages(){
        //List<Integer> messages = npc.getMessages();

        for(int i = 0; i < messages.size(); ++i){
            String message = getString(messages.get(i));
            addMessage(message, i, messages.size() - 1);
        }
    }

    private void addMessage(String m, int index, int maxIndex){
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

        TextView message = new TextView(this);
        message.setLayoutParams(lparams);
        message.setId(index);
        message.setText(m);
        message.setBackground(getDrawable(index % 2 == 0 ? R.drawable.message_from_npc : R.drawable.message_from_user));
        message.setTextColor(getResources().getColor(R.color.colorGreyBlack));
        message.setTextSize(20);

        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout_npc);
        constraintLayout.addView(message);
    }
}
