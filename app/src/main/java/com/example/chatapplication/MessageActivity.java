package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.chatapplication.Adapter.MessageAdapter;
import com.example.chatapplication.Model.Chats;
import com.example.chatapplication.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;

    DatabaseReference databaseReference;

    TextView username;
    ImageView profile_image;

    ImageButton sendButton;
    EditText _message;


Intent intent;

RecyclerView recyclerView;
List<Chats> chats;
MessageAdapter messageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

    username=findViewById(R.id.username);
    profile_image=findViewById(R.id.profile_image);

_message=findViewById(R.id.message);

    intent=getIntent();

    recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
    recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


    final String userid=intent.getStringExtra("userid");


    Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("");
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sendButton=(ImageButton)findViewById(R.id.sendbutton);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    });

    sendButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String msg=_message.getText().toString();
            if (!msg.equalsIgnoreCase("")){
                sendMessage(firebaseUser.getUid(),userid,msg);
            }else {
                Toast.makeText(MessageActivity.this, "You can't send an empty message!!!", Toast.LENGTH_SHORT).show();
            }
            _message.setText("");
        }
    });

    firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(userid);

databaseReference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        Users user=dataSnapshot.getValue(Users.class);
        username.setText(user.getUsername());
        if (user.getImageUrl().equalsIgnoreCase("default")){
            profile_image.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(MessageActivity.this).load(user.getImageUrl()).into(profile_image);
        }

        readMessages(firebaseUser.getUid(),userid,user.getImageUrl());
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});



    }

    private void sendMessage(String sender,String receiver,String message){
DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();


        HashMap<String ,Object> hashMap=new HashMap<>();

        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        databaseReference.child("Chats").push().setValue(hashMap);


    }
private  void readMessages(final String myid,final String userid,final String imageurl){
chats =new ArrayList<>();

databaseReference=FirebaseDatabase.getInstance().getReference("Chats");
databaseReference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        chats.clear();

        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
            Chats chat=snapshot.getValue(Chats.class);
            if (chat.getReceiver().equals(myid)&&chat.getSender().equals(userid)||chat.getReceiver().equals(userid)&&chat.getSender().equals(myid)){
                chats.add(chat);
            }
            messageAdapter=new MessageAdapter(MessageActivity.this,chats,imageurl);
            recyclerView.setAdapter(messageAdapter);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});







}


}