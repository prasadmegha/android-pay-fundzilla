package com.example.stripe_google_pay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateCampaignActivity extends AppCompatActivity {
    String title = null;
    int goal = -1;
    String blurb = null;
    String body = null;
    String stripeId=null;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_me);
        Bundle extras = getIntent().getExtras();
        final TextInputEditText titleText = (TextInputEditText) findViewById(R.id.title);
        final TextInputEditText goalText = (TextInputEditText) findViewById(R.id.goal);
        final TextInputEditText blurbText = (TextInputEditText) findViewById(R.id.blurb);
        final TextInputEditText bodyText = (TextInputEditText) findViewById(R.id.bodyText);

        stripeId = extras.getString("stripeId");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        FirebaseApp.initializeApp(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference campaigns = mDatabase.child("campaigns");

        Button loginButton = findViewById(R.id.create);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Campaign campaign = new Campaign();
                campaign.raised=0;
                campaign.goal = Integer.parseInt(goalText.getText().toString());
                campaign.stripeId= stripeId;
                campaign.title=titleText.getText().toString();
                campaign.shortHeadline=blurbText.getText().toString();
                campaign.body = bodyText.getText().toString();
                DatabaseReference campaignNode = mDatabase.child("campaigns").push();
                campaignNode.setValue(campaign);
                Intent intent = new Intent(CreateCampaignActivity.this,
                        CampaignListActivity.class);
                intent.putExtra("stripeId", stripeId);
                startActivity(intent);
            }
        });
    }

}
