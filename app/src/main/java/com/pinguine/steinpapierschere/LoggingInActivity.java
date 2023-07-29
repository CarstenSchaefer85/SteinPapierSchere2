package com.pinguine.steinpapierschere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoggingInActivity extends AppCompatActivity {

    EditText etxt_play_name;
    Button btn_log_in;

    String playerName = "";

    FirebaseDatabase database;
    DatabaseReference playerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logging_in);

        etxt_play_name = findViewById(R.id.etxt_play_name);
        btn_log_in = findViewById(R.id.btn_log_in);

        database = FirebaseDatabase.getInstance();

        btn_log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // logging the player in
                playerName = etxt_play_name.getText().toString();
                if (!playerName.equals("")) {
                    btn_log_in.setText(R.string.log_in);
                    btn_log_in.setEnabled(false);
                    playerRef = database.getReference("players/"+playerName);
                    addEventListener();
                    playerRef.setValue("");
                }
            }
        });
    }

    private void addEventListener() {
        // read from database
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Intent intent = new Intent(LoggingInActivity.this, ChooseRoomActivity.class);
                intent.putExtra("playerName",playerName);
                startActivity(intent);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // error
                btn_log_in.setText(R.string.log_in);
                btn_log_in.setEnabled(true);
                Toast.makeText(LoggingInActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
