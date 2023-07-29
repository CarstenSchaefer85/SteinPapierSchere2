package com.pinguine.steinpapierschere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WaitActivity extends AppCompatActivity implements ValueEventListener{

    String playerName = "";
    String roomName = "";

    FirebaseDatabase database;
    DatabaseReference roomRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playerName = extras.getString("playerName");
            roomName = extras.getString("roomName");
        }
        database = FirebaseDatabase.getInstance();
        roomRef = database.getReference("rooms/"+roomName);
       // roomRef.addListenerForSingleValueEvent(this);
        roomRef.addValueEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        boolean player1Ready = false;
        boolean player2Ready = false;
        if (dataSnapshot.child("player1").exists()) {
            player1Ready = true;
        }
        if (dataSnapshot.child("player2").exists()) {
            player2Ready = true;
        }
        //player2Ready = true;
        if (player1Ready && player2Ready) {
            // start game
            Intent intent = new Intent(getApplicationContext(), ChooseResultActivity.class);
            intent.putExtra("playerName",playerName);
            intent.putExtra("roomName",roomName);
            startActivity(intent);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
