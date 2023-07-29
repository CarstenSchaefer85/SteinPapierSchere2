package com.pinguine.steinpapierschere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChooseRoomActivity extends AppCompatActivity {

    ListView listView_rooms;
    Button btn_choose_room;

    List<String> roomsList;

    String playerName = "";
    String roomName = "";

    FirebaseDatabase database;
    DatabaseReference roomRef;
    DatabaseReference roomsRef;
    DatabaseReference playerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_room);

        database = FirebaseDatabase.getInstance();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playerName = extras.getString("playerName");
        }

        listView_rooms = findViewById(R.id.listView_rooms);
        btn_choose_room = findViewById(R.id.btn_choose_room);
        btn_choose_room.setText(R.string.create_room);

        // all existing available rooms
        roomsList = new ArrayList<>();

        btn_choose_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create room and add yourself as player1
                btn_choose_room.setEnabled(false);
                roomName = "room"+(roomsList.size()+1);

                roomRef = database.getReference("rooms/"+roomName);
                roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.child("player1").exists()) {
                            playerRef = database.getReference("rooms/"+roomName+"/player1");
                            playerRef.setValue(playerName);
                        } else if (!dataSnapshot.child("player2").exists()) {
                            playerRef = database.getReference("rooms/"+roomName+"/player2");
                            playerRef.setValue(playerName);
                        } else {
                            // error room has already two players!
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                addRoomEventListener();
            }
        });

        listView_rooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // join an existing room and add yourself as player2
                roomName = roomsList.get(position);
                roomRef = database.getReference("rooms/"+roomName);
                roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.child("player1").exists()) {
                            playerRef = database.getReference("rooms/"+roomName+"/player1");
                            playerRef.setValue(playerName);
                        } else if (!dataSnapshot.child("player2").exists()) {
                            playerRef = database.getReference("rooms/"+roomName+"/player2");
                            playerRef.setValue(playerName);
                        } else {
                            // error room has already two players!
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                addRoomEventListener();
            }
        });

        // show if new room is available
        addRoomsEventListener();
    }

    private void addRoomEventListener() {
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // join the room
                btn_choose_room.setText(R.string.create_room);
                btn_choose_room.setEnabled(true);
                Intent intent = new Intent(getApplicationContext(), WaitActivity.class);
                intent.putExtra("playerName",playerName);
                intent.putExtra("roomName",roomName);
                startActivity(intent);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // error
                btn_choose_room.setText(R.string.create_room);
                btn_choose_room.setEnabled(true);
                Toast.makeText(ChooseRoomActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addRoomsEventListener() {
        roomsRef = database.getReference("rooms");
        roomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // show list of rooms
                roomsList.clear();
                Iterable<DataSnapshot> rooms = dataSnapshot.getChildren();
                for(DataSnapshot snapshot : rooms) {
                    roomsList.add(snapshot.getKey());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ChooseRoomActivity.this,
                            android.R.layout.simple_list_item_1, roomsList);
                    listView_rooms.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // error - nothing
            }
        });
    }
}
