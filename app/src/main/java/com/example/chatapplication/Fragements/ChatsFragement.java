package com.example.chatapplication.Fragements;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapplication.Adapter.UserAdapter;
import com.example.chatapplication.Model.Chats;
import com.example.chatapplication.Model.Users;
import com.example.chatapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragement extends Fragment {

    private UserAdapter userAdapter;
    private List<Users> mUsers;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    private List<String> userlist;
    private RecyclerView recyclerView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userlist=new ArrayList<>();
         View view =inflater.inflate(R.layout.fragment_chats_fragement  , container, false);
            recyclerView=view.findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

            databaseReference= FirebaseDatabase.getInstance().getReference("Chats");
            databaseReference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        userlist.clear();
        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
            Chats chats=snapshot.getValue(Chats.class);
            if (chats.getSender().equals(firebaseUser.getUid())){
                userlist.add(chats.getReceiver());
            }if (chats.getReceiver().equals(firebaseUser.getUid())){
                userlist.add(chats.getSender());
            }
        }
        readChats();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

         return view;
    }

    private void readChats(){
        mUsers=new ArrayList<>();

        databaseReference=FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Users users=snapshot.getValue(Users.class);

                    //Display one user from the list
                    for (String id:userlist){
                        assert users != null;
                        if(users.getId().equals(id)){
                            if(mUsers.size()!=0){
                                int flag=0;
                                for(Users u : mUsers) {
                                    if (users.getId().equals(u.getId())) {
                                        flag = 1;
                                        break;
                                    }
                                }
                                if(flag==0)
                                    mUsers.add(users);
                            }else{

                                mUsers.add(users);
                            }
                        }
                    }
//                    for (String id :userlist){
//                        if (users.getId().equals(id)){
//                            if (mUsers.size()!=0){
//                                 for(Users users1 :new ArrayList<Users>(mUsers) ){
//                                    if (!users.getId().equals(users1.getId())){
//                                        mUsers.add(users);
//                                    }
//                                }
//                            }else {
//                                mUsers.add(users);
//                            }
//                        }
//                    }

                }
                userAdapter=new UserAdapter(getContext(),mUsers);
                recyclerView.setAdapter(userAdapter);
                recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}