package com.example.stripe_google_pay;

import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.identity.intents.model.UserAddress;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.CardInfo;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stripe.android.model.Token;

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


        mCampaignListView = (ListView) findViewById(R.id.campaign_list_view);
        FirebaseApp.initializeApp(this);
        DatabaseReference mDatabase =
                FirebaseDatabase.getInstance().getReference();

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


        /*
        userId = getIntent().getExtras().getString("userId");
        stripeId = getIntent().getExtras().getString("stripeId");
        if (userId == null) {
            throw new RuntimeException(" User id missing, can't proceed");
        }

        paymentsClient =
                Wallet.getPaymentsClient(this,
                        new Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                                .build());

        //Opens the funding page activity
        /*
        Button fundButton = findViewById(R.id.);
       fundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity3();
            }
        });


        findViewById(R.id.buy).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //PaymentUtils.testChargeToken();
                        payWithGoogle();
                    }
                });
                */
    }

    private void payWithGoogle() {
        PaymentDataRequest request = PaymentUtils.createPaymentDataRequest();
        if (request != null) {
            AutoResolveHelper.resolveTask(
                    paymentsClient.loadPaymentData(request),
                    PaymentActivity.this,
                    LOAD_PAYMENT_DATA_REQUEST_CODE);
        }
    }


    private void openActivity3() {
        Intent intent = new Intent(this, FundMeActivity.class);
        startActivity(intent);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        // You can get some data on the user's card, such as the brand and last 4 digits
                        CardInfo info = paymentData.getCardInfo();
                        // You can also pull the user address from the PaymentData object.
                        UserAddress address = paymentData.getShippingAddress();
                        // This is the raw JSON string version of your Stripe token.
                        String rawToken = paymentData.getPaymentMethodToken().getToken();

                        // Now that you have a Stripe token object, charge that by using the id
                        Token stripeToken = Token.fromString(rawToken);
                        if (stripeToken != null) {
                            PaymentUtils.chargeToken(stripeToken, stripeId);
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                    case AutoResolveHelper.RESULT_ERROR:
                        Status status = AutoResolveHelper.getStatusFromIntent(data);
                        // Log the status for debugging
                        // Generally there is no need to show an error to
                        // the user as the Google Payment API will do that
                        break;
                    default:
                        // Do nothing.
                }
                break; // Breaks the case LOAD_PAYMENT_DATA_REQUEST_CODE
            // Handle any other startActivityForResult calls you may have made.
            default:
                // Do nothing.
        }
    }
}
