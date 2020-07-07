package com.example.chatapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText username , email , password;
    DatabaseReference reference;
    Button btn_register,btn_login;
    FirebaseAuth auth;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

btn_login=(Button)findViewById(R.id.login);
        username=findViewById(R.id.username);
        email=findViewById(R.id.input_username);
        password=findViewById(R.id.password);

        btn_register=findViewById(R.id.signup);
        auth=FirebaseAuth.getInstance();


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username=username.getText().toString();
                String txt_email=email.getText().toString();
                String txt_password=password.getText().toString();

                if (TextUtils.isEmpty(txt_username)||TextUtils.isEmpty(txt_password)||TextUtils.isEmpty(txt_email)){
//                username.setError("Field is Empty");
//                email.setError("Field is Empty");
//                password.setError("Field is Empty");
                    Toast.makeText(RegisterActivity.this, "All Fields are required", Toast.LENGTH_SHORT).show();
                }else if(txt_password.length()<6){
                    Toast.makeText(RegisterActivity.this, "Pass Should be greater than 6 Characters", Toast.LENGTH_SHORT).show();

                }else {
                    Register(txt_username,txt_email,txt_password);

                }
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });
    }


    private void Register(final String username, String email, String password){

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser=auth.getCurrentUser();
                    String userId=firebaseUser.getUid();

                    reference= FirebaseDatabase.getInstance().getReference("Users").child(userId);


                    HashMap<String,String>  hashMap= new HashMap<>();
                    hashMap.put("id",userId);
                    hashMap.put("username",username);
                    hashMap.put("ImageUrl","default");

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            }
                        }
                    });

                }else {
                    Toast.makeText(RegisterActivity.this, "You Can't Register with this Email  or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
