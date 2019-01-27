package com.example.stripe_google_pay;
import android.content.Intent;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText email;
    EditText pass;
    private DatabaseReference mDatabase;
    private User currentUser;
    private static  String userId;
    private static String stripeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.emailText);
        pass = findViewById(R.id.pwText);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Write a message to the database
        FirebaseApp.initializeApp(this);
        Button loginButton = findViewById(R.id.loginBtn);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        loginButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        DatabaseReference users = mDatabase.child("users");
                        Query queryRef =
                                users.orderByChild("email")
                                        .equalTo(email.getText().toString())
                                .limitToFirst(1);

                        queryRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.hasChildren()) {
                                            createNewCustomerRecord();
                                            openActivity2();
                                            return;
                                        }

                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                currentUser = snapshot.getValue(User.class);
                                             openActivity2();
                                                return;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                    }
                });
    }

    private void openActivity2() {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("userId", currentUser.userId);
        intent.putExtra("stripeId", currentUser.stripeId);
        startActivity(intent);
    }

    private void createNewCustomerRecord() {
        stripeId = PaymentUtils.createStripeCustomer(email.getText().toString());
        DatabaseReference userNode = mDatabase.child("users").push();
        currentUser = new User(email.getText().toString(),
                pass.getText().toString(),stripeId, userNode.getKey());
        userNode.setValue(currentUser);
    }
}



