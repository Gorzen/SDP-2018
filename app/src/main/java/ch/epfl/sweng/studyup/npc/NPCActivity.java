package ch.epfl.sweng.studyup.npc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.Utils;

public class NPCActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_npc);
        Intent intent = getIntent();
        String npcName = intent.getStringExtra(Constants.NPC_ACTIVITY_INTENT_NAME);
        NPC npc = Utils.getNPCfromName(npcName);
        ImageView imageView = findViewById(R.id.npc_image);
        imageView.setImageResource(npc.getImage());
    }

}
