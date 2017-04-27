package com.guc.wasel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity{

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ArrayAdapter<String> drawerListAdapter;
    private Form form;
    private MapsFragment mapsFragment;
    static int state = 0;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.



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


        form = new Form();
        mapsFragment = new MapsFragment();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        drawerListAdapter = new ArrayAdapter<String>(this, R.layout.drawer_list_item);
        drawerListAdapter.add("Form");
        drawerListAdapter.add("Maps");
        drawerListAdapter.add("sign out");

        drawerListAdapter.notifyDataSetChanged();

        mDrawerList.setAdapter(drawerListAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, form).commit();
                    mDrawerLayout.closeDrawer(Gravity.LEFT,true);
                    state=1;
                } else if(i==1) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, mapsFragment).commit();
                    mDrawerLayout.closeDrawer(Gravity.LEFT,true);
                    state=0;
                }else{
                    MainActivity.setIsAdmin(false);
                    mAuth.signOut();
                }
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

    }



    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}





