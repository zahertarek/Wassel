package com.guc.wasel;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.auth.UserProfileChangeRequest;

public class signUpActivity extends AppCompatActivity {

    private EditText emailBox;
    private EditText passwordBox;
    private EditText password2Box;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("User Status", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("User Status", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };




        emailBox = (EditText)findViewById(R.id.signup_email);
        passwordBox = (EditText) findViewById(R.id.signup_password);
        password2Box = (EditText) findViewById(R.id.signup_password2);
        registerButton = (Button) findViewById(R.id.signup_btn);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });


    }

    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }


    private void createUser(){
        mAuth.createUserWithEmailAndPassword(emailBox.getText().toString(),passwordBox.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(signUpActivity.this,"SignUp Failed please try again later",
                                    Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getBaseContext(),"You are now Registered Please Login",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getBaseContext(),MainActivity.class);
                            startActivity(intent);
                        }

                        // ...
                    }
                });
    }
}
