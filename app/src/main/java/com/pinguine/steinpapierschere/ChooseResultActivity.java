package com.pinguine.steinpapierschere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChooseResultActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton btn_stein, btn_papier, btn_schere;
    TextView txt_winner;

    String playerName = "";
    String roomName = "";

    FirebaseDatabase database;
    DatabaseReference messageRef;

    final int ID_STEIN = 11;
    final int ID_PAPIER = 21;
    final int ID_SCHERE = 31;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_result);

        btn_stein = findViewById(R.id.btn_stein);
        btn_stein.setOnClickListener(this);
        btn_papier = findViewById(R.id.btn_papier);
        btn_papier.setOnClickListener(this);
        btn_schere = findViewById(R.id.btn_schere);
        btn_schere.setOnClickListener(this);

        txt_winner = findViewById(R.id.txt_winner);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playerName = extras.getString("playerName");
            roomName = extras.getString("roomName");
        }

        database = FirebaseDatabase.getInstance();
        messageRef = database.getReference("rooms/"+roomName+"/"+playerName);
    }

    @Override
    public void onClick(View v) {
        int player1_result_ID;
        // player 1
        int ce = v.getId();
        switch (ce) {
            case R.id.btn_stein:
                player1_result_ID = ID_STEIN;
                break;
            case R.id.btn_papier:
                player1_result_ID = ID_PAPIER;
                break;
            case R.id.btn_schere:
                player1_result_ID = ID_SCHERE;
                break;
            default:
                player1_result_ID = -1;
                break;
        }
        if (player1_result_ID != -1) {
            messageRef.setValue(player1_result_ID);
            changeActivityEventListener();
        }
    }

    private void changeActivityEventListener() {
        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Intent intent = new Intent(ChooseResultActivity.this, ResultActivity.class);
                intent.putExtra("roomName",roomName);
                startActivity(intent);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // error
                //Toast.makeText(ChooseRoomActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
