package com.guc.wasel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * This class defines the MainActivity, where the user is allowed to sign in to the application
 */
public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private TextView signUpLink;
    private TextView forgotPasswordLink;
    private EditText emailBox;
    private EditText passwordBox;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ProgressDialog progressDialog;

    /**
     * This method return true if the user has admin privileges, and false if not
     * @return isAdmin boolean
     */
    public static boolean isAdmin() {
        return isAdmin;
    }

    /**
     * this method check if the current user is admin and set the isAdmin variable to the correct value
     * @param isAdmin boolean
     */
    public static void setIsAdmin(boolean isAdmin) {
        MainActivity.isAdmin = isAdmin;
    }

    private static boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if( getIntent().getBooleanExtra("Exit me", false)){
            finish();
            return; // add this to prevent from doing unnecessary stuffs
        }

        initializeProgressDialogue();

        checkUserStatus();

        declareActivityViews();

        setActivityListeners();

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

    /**
     * This method connects to Firebase Authentication service to sign in the user whith the provided credentials.
     */
    private void signIn() {
        mAuth.signInWithEmailAndPassword(emailBox.getText().toString(), passwordBox.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            progressDialog.hide();
                            Toast.makeText(MainActivity.this, "Login Failed",
                                    Toast.LENGTH_LONG).show();

                        } else {
                            progressDialog.hide();
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

                            Intent intent = new Intent(getBaseContext(), MapsActivity.class);

                            startActivity(intent);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to exit ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.exit(0);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    /**
     * This method checks the current status of the user, if he is already loggedIn the application will redirect to the MapsFragment.
     */
    private void checkUserStatus(){
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                    startActivity(intent);

                } else {
                    // User is signed out
                    Log.d("User Status", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    /**
     * This method is responsible of declaring the views instant variables of this Activity to their corresponding view in the layout files, using findViewById() method.
     */
    private void declareActivityViews(){
        loginButton = (Button) findViewById(R.id.login_btn);
        emailBox = (EditText) findViewById(R.id.email_login);
        passwordBox = (EditText) findViewById(R.id.password_login);
        signUpLink = (TextView) findViewById(R.id.signup_text);
        forgotPasswordLink = (TextView) findViewById(R.id.forget_link);
    }

    /**
     * This method initialize the progress circle when the user clicks on the signin button
     */
    private void initializeProgressDialogue(){
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Logging In");
        progressDialog.setCancelable(false);
    }

    /**
     * This method set the listeners of the views.
     */
    private void setActivityListeners(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailBox.getText().toString().isEmpty()) {
                    Toast.makeText(getBaseContext(), "Please Enter a Valid Email", Toast.LENGTH_LONG).show();
                } else if (passwordBox.getText().toString().isEmpty()) {
                    Toast.makeText(getBaseContext(), "Please Enter a Valid Password", Toast.LENGTH_LONG).show();
                } else {

                    progressDialog.show();
                    signIn();
                }
            }
        });

        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), signUpActivity.class);
                startActivity(intent);
            }
        });

        forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}


