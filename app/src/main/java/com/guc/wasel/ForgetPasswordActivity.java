package com.guc.wasel;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * This class defines the ForgetPasswordActivity, This Activity allows the user to enter his email, to receive a reset passwoed email.
 */
public class ForgetPasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText emailText;
    private Button resetButton;

    /**
     * This method is exectuted once the Activity is created, where views instand variables are declared, listeners are assigned.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        checkUserStatus();

        declareActivityViews();

        setResetButtonListener();

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
     * This method connect to the Firebase Authentication system to check if the user is loggedIn or not, then the status of the current user will be stated in the logs
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
     * This method is responsible of declary the instant variables of the views, and assigning them to their corresponding view in the xml File.
     */
    private void declareActivityViews(){
        emailText = (EditText) findViewById(R.id.email_reset);
        resetButton = (Button) findViewById(R.id.forget_email_btn);
    }

    /**
     * This method assign the listener of the reset button, so the application can connect to the Firebase Authentication System to request a reset email to the user.
     */
    private void setResetButtonListener(){
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.sendPasswordResetEmail(emailText.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getBaseContext(),"Check your Email To reset your password",Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }
                        });
            }
        });
    }

}

