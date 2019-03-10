package com.example.stripe_google_pay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CampaignActivity extends AppCompatActivity {
    private String campaignId = null;
    Campaign campaignDetail = new Campaign();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        FirebaseApp.initializeApp(this);
        DatabaseReference mDatabase =
                FirebaseDatabase.getInstance().getReference();
        Bundle extras = getIntent().getExtras();
        campaignId = extras.getString("campaignID");

        mDatabase.child("campaigns").child(campaignId).addValueEventListener(
                new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        campaignDetail = dataSnapshot.getValue(Campaign.class);
                        fillLayout();
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }

        );


    }

    private void fillLayout() {
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(campaignDetail.title);

        TextView  textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setText(campaignDetail.body);

    }
}