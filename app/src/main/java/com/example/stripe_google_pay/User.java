package com.example.stripe_google_pay;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String password;
    public String email;
    public String stripeId;
    public String userId;

    public User() {}

    public User(String email, String password, String stripeId, String userId) {
        this.password = password;
        this.email = email;
        this.stripeId = stripeId;
        this.userId = userId;
    }
}
