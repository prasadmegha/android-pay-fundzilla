package com.example.stripe_google_pay;

import android.content.Intent;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity {
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 99;
    private static PaymentsClient paymentsClient;
    private static  String userId;
    private static String stripeId;
    private ListView mCampaignListView;
    private ArrayList<Campaign> activeCampaigns = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Bundle extras = getIntent().getExtras();
        stripeId = extras.getString("stripeId");
        mCampaignListView = (ListView) findViewById(R.id.campaign_list_view);
        FirebaseApp.initializeApp(this);
        DatabaseReference mDatabase =
                FirebaseDatabase.getInstance().getReference();

        FloatingActionButton floatingActionButton =
                (FloatingActionButton) findViewById(R.id.floating_action_button);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentActivity.this,
                        FundMeActivity.class);
                intent.putExtra("stripeId", stripeId);
                startActivity(intent);
            }
        });

        mDatabase.child("campaigns").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        activeCampaigns = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Campaign campaign = snapshot.getValue(Campaign.class);
                            String campaignId = snapshot.getKey();
                            campaign.setId(campaignId);
                            activeCampaigns.add(campaign);
                        }

                        mCampaignListView
                                .setAdapter(new CampaignListAdapter(
                                        PaymentActivity.this, activeCampaigns));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }


}