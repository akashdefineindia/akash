package com.define.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email,password,name,password1;
    private Button siginin;
    private String emailString,passwordString,nameString,numberString;
    private DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        email=(EditText) findViewById(R.id.email);
        password=(EditText) findViewById(R.id.password);
        name=(EditText) findViewById(R.id.name);
        password1=(EditText) findViewById(R.id.mobile);
        siginin=(Button) findViewById(R.id.signin);
        siginin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                siginUser();
            }
        });
    }

    private void siginUser() {
        emailString=email.getText().toString();
        passwordString=password.getText().toString();
        mAuth.createUserWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e("Ok", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid=user.getUid();
                            Log.e("uid",uid);

                            ref= FirebaseDatabase.getInstance().getReference().child("user").child(uid);
                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put("name","akash");
                            hashMap.put("number","8889477954");
                            hashMap.put("place","indore");
                            hashMap.put("gender","male");
                            String device_token = FirebaseInstanceId.getInstance().getToken();
                            ref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Authentication success.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("error", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}
