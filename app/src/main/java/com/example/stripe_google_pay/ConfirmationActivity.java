package com.example.stripe_google_pay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        Bundle extras = getIntent().getExtras();
        final String stripeId = extras.getString("stripeId");

        findViewById(R.id.button2).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ConfirmationActivity.this,
                                PaymentActivity.class);
                        intent.putExtra("stripeId", stripeId);
                        startActivity(intent);
                    }
                });
    }
}
