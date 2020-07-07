package com.example.chatapplication;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatapplication.Fragements.ChatsFragement;
import com.example.chatapplication.Fragements.ProfileFragment;
import com.example.chatapplication.Fragements.UsersFragment;
import com.example.chatapplication.Model.Users;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    CircleImageView profile_image;
    TextView username;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    DatabaseReference reference;
    Toolbar toolbar;
    Button logout_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profile_image=(CircleImageView)findViewById(R.id.profile_image);
        username=(TextView)findViewById(R.id.username);

        TabLayout tableLayout=(TabLayout)findViewById(R.id.tableLayout);
        ViewPager viewPage=(ViewPager)findViewById(R.id.view_pager);


        ViewpagerAdapter viewpagerAdapter=new ViewpagerAdapter(getSupportFragmentManager());

        viewpagerAdapter.addFragement(new ChatsFragement(),"Chats");
        viewpagerAdapter.addFragement(new UsersFragment(),"Users");
        viewpagerAdapter.addFragement(new ProfileFragment(),"Profile");

        viewPage.setAdapter(viewpagerAdapter);
        tableLayout.setupWithViewPager(viewPage);

//
//
//logout_button=(Button)findViewById(R.id.logout_button);
        toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


//
//        logout_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(MainActivity.this,StartActivity.class));
//                finish();
//            }
//        });

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Users users =snapshot.getValue(Users.class);
if (users.getUsername()!=null){
    username.setText(users.getUsername());
}

                if (users.getImageUrl().equalsIgnoreCase("default")&& users.getImageUrl()!=null){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }else
                {
                    Glide.with(MainActivity.this).load(users.getImageUrl()).into(profile_image);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,StartActivity.class));
                finish();
                return true;

        }
        return false;

    }


    public class ViewpagerAdapter extends FragmentPagerAdapter{

        ArrayList<Fragment>fragments;
        ArrayList<String> titles;



         ViewpagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();


        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        public void addFragement(Fragment fragment, String title){

             fragments.add(fragment);
             titles.add(title);
        }

    }



}
