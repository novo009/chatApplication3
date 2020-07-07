package com.example.chatapplication.Fragements;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatapplication.Model.Users;
import com.example.chatapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    ImageView profile_image;
    TextView username;

    FirebaseUser firebaseUser;

    DatabaseReference databaseReference;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
View view=inflater.inflate(R.layout.fragment_profile, container, false);

profile_image=view.findViewById(R.id.profile_image);
username=(TextView)view.findViewById(R.id.username);

firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

databaseReference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        Users users=dataSnapshot.getValue(Users.class);

        username.setText(users.getUsername());
//if (users.getImageUrl()==null){
//    users.setImageUrl("default");
//}
        if (users.getImageUrl().equalsIgnoreCase("default")){
            profile_image.setImageResource(R.mipmap.ic_launcher);
        }else
        {
            Glide.with(getContext()).load(users.getImageUrl()).into(profile_image);
        }

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});




        return view;


    }
}