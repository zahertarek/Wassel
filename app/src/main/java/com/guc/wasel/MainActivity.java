package com.guc.wasel;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private TextView signUpLink;
    private TextView forgotPasswordLink;
    private EditText emailBox;
    private EditText passwordBox;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressBar spinner;

    public static boolean isAdmin() {
        return isAdmin;
    }

    public static void setIsAdmin(boolean isAdmin) {
        MainActivity.isAdmin = isAdmin;
    }

    private  static boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(getBaseContext(),MapsActivity.class);
                    startActivity(intent);

                } else {
                    // User is signed out
                    Log.d("User Status", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        loginButton        = (Button)   findViewById(R.id.login_btn);
        emailBox           = (EditText) findViewById(R.id.email_login);
        passwordBox        = (EditText) findViewById(R.id.password_login);
        signUpLink         = (TextView) findViewById(R.id.signup_text);
        forgotPasswordLink = (TextView) findViewById(R.id.forget_link);
        spinner            = (ProgressBar) findViewById(R.id.progress_login);



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailBox.getText().toString().isEmpty()){
                    Toast.makeText(getBaseContext(),"Please Enter a Valid Email",Toast.LENGTH_LONG).show();
                }else if(passwordBox.getText().toString().isEmpty()){
                    Toast.makeText(getBaseContext(),"Please Enter a Valid Password",Toast.LENGTH_LONG).show();
                } else {
                    spinner.setVisibility(View.VISIBLE);
                    signIn();
                }
            }
        });

        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),signUpActivity.class);
                startActivity(intent);
            }
        });

        forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    private void signIn(){
        mAuth.signInWithEmailAndPassword(emailBox.getText().toString(),passwordBox.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this,"Login Failed",
                                    Toast.LENGTH_LONG).show();
                            spinner.setVisibility(View.GONE);
                        }else{
                            DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Admin");

                            ref1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    ArrayList admins = (ArrayList) dataSnapshot.getValue();
                                    for (int i = 0; i < admins.size(); i++) {
                                        String entry = (String) admins.get(0);
                                        if (mAuth.getCurrentUser().getUid().equals(entry)) {
                                            isAdmin = true;
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            Intent intent = new Intent(getBaseContext(),MapsActivity.class);
                            spinner.setVisibility(View.GONE);
                            startActivity(intent);
                        }

                        // ...
                    }
                });
    }
}
