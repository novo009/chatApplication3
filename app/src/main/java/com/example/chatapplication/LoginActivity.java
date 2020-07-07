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
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity {

    EditText username , email , password;
    DatabaseReference reference;
    Button btn_register,btn_login;
    FirebaseAuth auth;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        email=findViewById(R.id.input_username);
        password=findViewById(R.id.password);

        btn_register=findViewById(R.id.signup);
        auth= FirebaseAuth.getInstance();
btn_login=(Button)findViewById(R.id.login);
btn_register.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
    }
});


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if ( TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_email)) {
//                username.setError("Field is Empty");
//                email.setError("Field is Empty");
//                password.setError("Field is Empty");
                    Toast.makeText(LoginActivity.this, "All Fields are required", Toast.LENGTH_SHORT).show();
                }else   {
                    auth.signInWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent intent =new Intent(LoginActivity.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();


                            }else  {
                                Toast.makeText(LoginActivity.this, "Authentication Failed ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }});


}
}