package com.pinguine.steinpapierschere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    final int ID_STEIN = 11;
    final int ID_PAPIER = 21;
    final int ID_SCHERE = 31;

    TextView txt_result;
    Button btn_next;
    ImageView img_p1, img_p2;

    String roomName = "";
    boolean game_finished;

    FirebaseDatabase database;
    DatabaseReference roomRef, playerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //  DisplayMetrics dm = new DisplayMetrics();
        //  getWindowManager().getDefaultDisplay().getMetrics(dm);
        //  int width = dm.widthPixels;
        //  int height = dm.heightPixels;
        //  getWindow().setLayout((int)(width*0.6),(int)(height*0.6));

        txt_result = findViewById(R.id.txt_result);
        btn_next = findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);
        img_p1 = findViewById(R.id.img_p1);
        img_p2 = findViewById(R.id.img_p2);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            roomName = extras.getString("roomName");
        }

        database = FirebaseDatabase.getInstance();
        roomRef = database.getReference("rooms/"+roomName);
        roomRef.addValueEventListener(this);
        //roomRef.addListenerForSingleValueEvent(this);
        game_finished = false;
    }

    @Override
    public void onClick(View v) {
        int ce = v.getId();
        if (ce == R.id.btn_next) {
         //   game_finished = true;
           // roomRef.addListenerForSingleValueEvent(this);
        //    finish();
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        // player1
        int player1Result = -1;
        if (dataSnapshot.child("player1").exists()) {
            String player1Name = dataSnapshot.child("player1").getValue(String.class);
            if (player1Name != null) {
                if (dataSnapshot.child(player1Name).exists()) {
                    DataSnapshot dataSnapshot_p1 = dataSnapshot.child(player1Name);
                    if (game_finished) {
                        //    playerRef = database.getReference("rooms/"+roomName+"/"+player1Name);
                        //    playerRef.setValue(-1);
                    } else if (dataSnapshot_p1.exists()) {
                        player1Result = dataSnapshot_p1.getValue(Integer.class);
                        switch (player1Result) {
                            case ID_STEIN:
                                img_p1.setBackground(getDrawable(R.drawable.stein));
                                break;
                            case ID_PAPIER:
                                img_p1.setBackground(getDrawable(R.drawable.papier));
                                break;
                            case ID_SCHERE:
                                img_p1.setBackground(getDrawable(R.drawable.schere));
                                break;
                        }
                    }
                }
            }
        }
        // player2
        int player2Result = -1;
        if (dataSnapshot.child("player2").exists()) {
            String player2Name = dataSnapshot.child("player2").getValue(String.class);
            if (player2Name != null) {
                if (dataSnapshot.child(player2Name).exists()) {
                    DataSnapshot dataSnapshot_p2 = dataSnapshot.child(player2Name);
                    if (game_finished) {
                        //    playerRef = database.getReference("rooms/"+roomName+"/"+player2Name);
                        //    playerRef.setValue(-1);
                    } else if (dataSnapshot_p2.exists()) {
                        player2Result = dataSnapshot_p2.getValue(Integer.class);
                        switch (player2Result) {
                            case ID_STEIN:
                                img_p2.setBackground(getDrawable(R.drawable.stein));
                                break;
                            case ID_PAPIER:
                                img_p2.setBackground(getDrawable(R.drawable.papier));
                                break;
                            case ID_SCHERE:
                                img_p2.setBackground(getDrawable(R.drawable.schere));
                                break;
                        }
                    }
                }
            }
        }
        // winner
        if ((player1Result != -1) && (player2Result != -1)) {
            // solve game
            int winner_ID = calcWinner(player1Result, player2Result);
            // output
            switch (winner_ID) {
                case 0:
                    txt_result.setText(R.string.draw);
                    break;
                case 1:
                    txt_result.setText(R.string.player1_wins);
                    break;
                case 2:
                    txt_result.setText(R.string.player2_wins);
                    break;
                default:
                    // Error
                    break;
            }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    private int calcWinner(int player1_result_ID, int player2_result_ID) {
        if (player1_result_ID == ID_STEIN) {
            if (player2_result_ID == ID_STEIN) {
                return 0;
            } else if (player2_result_ID == ID_PAPIER) {
                return 2;
            } else if (player2_result_ID == ID_SCHERE) {
                return 1;
            } else {
                return -1;
            }
        } else if (player1_result_ID == ID_PAPIER) {
            if (player2_result_ID == ID_STEIN) {
                return 1;
            } else if (player2_result_ID == ID_PAPIER) {
                return 0;
            } else if (player2_result_ID == ID_SCHERE) {
                return 2;
            } else {
                return -1;
            }
        } else if (player1_result_ID == ID_SCHERE) {
            if (player2_result_ID == ID_STEIN) {
                return 2;
            } else if (player2_result_ID == ID_PAPIER) {
                return 1;
            } else if (player2_result_ID == ID_SCHERE) {
                return 0;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }
}
