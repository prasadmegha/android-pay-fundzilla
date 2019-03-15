package com.example.stripe_google_pay;

import com.google.android.gms.wallet.CardRequirements;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.WalletConstants;
import com.stripe.Stripe;
import com.stripe.android.model.Token;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PaymentUtils {
    public static PaymentMethodTokenizationParameters createTokenizationParameters() {
        return PaymentMethodTokenizationParameters.newBuilder()
                .setPaymentMethodTokenizationType(WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_PAYMENT_GATEWAY)
                .addParameter("gateway", "stripe")
                .addParameter("stripe:publishableKey", "pk_test_TNv1f83o32VzJhWRLevJQYtD")
                .addParameter("stripe:version", "2018-11-08")
                .build();
    }

    public static PaymentDataRequest createPaymentDataRequest() {
        PaymentDataRequest.Builder request =
                PaymentDataRequest.newBuilder()
                        .setTransactionInfo(
                                TransactionInfo.newBuilder()
                                        .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                                        .setTotalPrice("1")
                                        .setCurrencyCode("USD")
                                        .build())
                        .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_CARD)
                        .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD)
                        .setCardRequirements(
                                CardRequirements.newBuilder()
                                        .addAllowedCardNetworks(Arrays.asList(
                                                WalletConstants.CARD_NETWORK_AMEX,
                                                WalletConstants.CARD_NETWORK_DISCOVER,
                                                WalletConstants.CARD_NETWORK_VISA,
                                                WalletConstants.CARD_NETWORK_MASTERCARD))
                                        .build());

        request.setPaymentMethodTokenizationParameters(createTokenizationParameters());
        return request.build();
    }

    public static void chargeToken(Token stripeToken, String accountId, int donation) {
        //Token stripeToken
        // This chargeToken function is a call to your own server, which should then connect
        // to Stripe's API to finish the charge.

        // Set your secret key: remember to change this to your live secret key in production
// See your keys here: https://dashboard.stripe.com/account/apikeys
        Stripe.apiKey= "sk_test_XdFiSYM0HYhnhv7zuXCOxL7n";
// Token is created using Checkout or Elements!
// Get the payment token ID submitted by the form:
        String token = stripeToken.getId();
        //String token = "tok_visa";

        Map<String, Object> params = new HashMap<>();
        params.put("amount", donation*100);
        params.put("currency", "usd");
        params.put("description", "Example charge");
        params.put("source", token);
        RequestOptions requestOptions = RequestOptions.builder()
                .setStripeAccount(accountId)
                .build();
        try {
            Charge charge = Charge.create(params, requestOptions);
        } catch (StripeException e) {
            throw new RuntimeException("Stripe-api threw an exception " + e);
        }
    }
    // Stripe custom account creation request
    public static String createStripeCustomer(String email) {
        // Set your secret key: remember to change this to your live secret key in production
         // See your keys here: https://dashboard.stripe.com/account/apikeys
        Stripe.apiKey = "sk_test_XdFiSYM0HYhnhv7zuXCOxL7n";


        //store details like country, email, type in params
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("country", "US");
        params.put("type", "custom");
        params.put("email", email);

        //Create account by calling create function and passing params as parameter and return ID using function getID()
        Account acct;
        try {
            acct = Account.create(params);
        } catch (StripeException e) {
            throw new RuntimeException("Stripe-api threw an exception " + e);
        }

        return acct.getId();
    }



}
