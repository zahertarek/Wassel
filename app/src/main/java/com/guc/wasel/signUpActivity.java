package com.guc.wasel;

import android.app.ProgressDialog;
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

/**
 * This cass define the signUpActivity, where users are allowed to register on the application
 */
public class signUpActivity extends AppCompatActivity {

    private EditText emailBox;
    private EditText passwordBox;
    private EditText password2Box;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        checkUserStatus();



        initializeProgressDialog();

        declareActivityViews();


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
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

    /**
     * This method connect to Firebase Authentication System to add a new user.
     */
    private void createUser(){
        mAuth.createUserWithEmailAndPassword(emailBox.getText().toString(),passwordBox.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            progressDialog.hide();
                            Toast.makeText(signUpActivity.this,"SignUp Failed please try again later",
                                    Toast.LENGTH_LONG).show();
                        }else{
                            progressDialog.hide();
                            Toast.makeText(getBaseContext(),"You are now Registered Please Login",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getBaseContext(),MainActivity.class);
                            startActivity(intent);
                        }

                        // ...
                    }
                });
    }

    /**
     * This method log the current user status
     */
    private void checkUserStatus(){
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
    }

    /**
     * This method creates a ProgressDialog that appears when the user clicks on the signup button
     */
    private void initializeProgressDialog(){
        progressDialog = new ProgressDialog(signUpActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Registering your details");
    }

    /**
     * This method is responsible of declaring the views instant variables of this Activity to their corresponding view in the layout files, using findViewById() method.
     */
    private void declareActivityViews(){
        emailBox = (EditText)findViewById(R.id.signup_email);
        passwordBox = (EditText) findViewById(R.id.signup_password);
        password2Box = (EditText) findViewById(R.id.signup_password2);
        registerButton = (Button) findViewById(R.id.signup_btn);
    }
}
