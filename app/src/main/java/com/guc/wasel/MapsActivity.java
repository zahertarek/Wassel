package com.guc.wasel;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;

/**
 * This class defines the MapsActivity.
 */
public class MapsActivity extends FragmentActivity{


    private ArrayAdapter<String> drawerListAdapter;
    private ActionBarDrawerToggle drawerToggle;
    private Form form;
    private MapsFragment mapsFragment;
    static int state = 0;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button sideButton;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.



        checkUserStatus();


        form = new Form();
        mapsFragment = new MapsFragment();


        final Drawer drawer = buildSideDrawer();

        sideButton = (Button) findViewById(R.id.side_button);

        sideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               drawer.openDrawer();
            }
        });


    }




    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(state == 0)
            fragmentManager.beginTransaction().replace(R.id.content_frame, mapsFragment).commit();
        else
            fragmentManager.beginTransaction().replace(R.id.content_frame, form).commit();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to exit ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Exit me", true);
                startActivity(intent);
                finish();
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

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    /**
     * This method check the current status of the user, if he is not logged in the application is redirected to the MainActivity, if he is logged in the isAdmin Static variable is set based on the correct value.
     */
    private void checkUserStatus(){
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Admin");

                    ref1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList admins = (ArrayList) dataSnapshot.getValue();
                            for (int i = 0; i < admins.size(); i++) {
                                String entry = (String) admins.get(0);
                                if (mAuth.getCurrentUser().getUid().equals(entry)) {
                                    MainActivity.setIsAdmin(true);
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    Intent intent = new Intent(getBaseContext(),MainActivity.class);
                    startActivity(intent);
                }

            }
        };
    }

    /**
     * This Method initialize the navigation bar, assigns its listeners so the user can navigate inside the application.
     * @return Side Navigation Bar
     */
    private Drawer buildSideDrawer(){
        PrimaryDrawerItem addPoiItem = new PrimaryDrawerItem().withIdentifier(1).withName("Add Point of Interest");
        PrimaryDrawerItem mapsItem   = new PrimaryDrawerItem().withIdentifier(2).withName("Maps");
        PrimaryDrawerItem signOutItem  = new PrimaryDrawerItem().withIdentifier(3).withName("Sign out");
        final Drawer drawer =  new DrawerBuilder().withActivity(this).addDrawerItems(addPoiItem,mapsItem,signOutItem).build();
        drawer.setSelectionAtPosition(1);
        drawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                if (position == 0) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, form).commit();
                    state=1;
                    drawer.closeDrawer();
                    return true;
                } else if(position==1) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, mapsFragment).commit();
                    state=0;
                    drawer.closeDrawer();
                    return true;

                }else{
                    MainActivity.setIsAdmin(false);
                    mAuth.signOut();
                    drawer.closeDrawer();
                    return true;
                }

            }
        });
        return drawer;
    }
}





