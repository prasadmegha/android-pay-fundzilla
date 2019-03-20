package com.example.stripe_google_pay;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.identity.intents.model.UserAddress;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.CardInfo;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stripe.android.model.Token;


public class CampaignActivity extends AppCompatActivity {
    private String campaignId = null;
    Campaign campaignDetail = new Campaign();
    EditText donation;
    private static PaymentsClient paymentsClient;
    DatabaseReference mDatabase = null;
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        FirebaseApp.initializeApp(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Bundle extras = getIntent().getExtras();
        campaignId = extras.getString("campaignID");
        donation = (EditText) findViewById(R.id.amount);

        paymentsClient = Wallet.getPaymentsClient(this,
                new Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                        .build());

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

         findViewById(R.id.button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        payWithGoogle();
                    }
                });
    }

    private void fillLayout() {
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(campaignDetail.title);

        TextView  textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setText(campaignDetail.body);

    }

    private void payWithGoogle() {
        PaymentDataRequest request = PaymentUtils.createPaymentDataRequest();
        if (request != null) {
            AutoResolveHelper.resolveTask(
                    paymentsClient.loadPaymentData(request),
                    CampaignActivity.this,
                    LOAD_PAYMENT_DATA_REQUEST_CODE);
        }
    }

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
                                int amount = Integer.parseInt(donation.getText().toString());
                                PaymentUtils.chargeToken(stripeToken, campaignDetail.stripeId, amount);
                                Intent intent = new Intent(this, PaymentConfirmationActivity.class);
                                intent.putExtra("stripeId", campaignDetail.stripeId);
                                startActivity(intent);
                                updateCampaignRaised(amount);
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

        void updateCampaignRaised(int amount) {
            campaignDetail.raised += amount;
            mDatabase.child("campaigns").child(campaignId).setValue(campaignDetail);
        }
}