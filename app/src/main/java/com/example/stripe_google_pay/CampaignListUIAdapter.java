package com.example.stripe_google_pay;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CampaignListUIAdapter extends BaseAdapter {
    //public constructor
    public CampaignListUIAdapter(Context context, ArrayList<Campaign> items) {
        this.context = context;
        this.items = items;
    }

    // override other abstract methods hereß
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // inflate the layout for each list row
            convertView = LayoutInflater.from(context).
                        inflate(R.layout.campaign_list_item, parent, false);
        }

        final Campaign campaign = getItem(position);
        ((TextView) convertView.findViewById(R.id.title1))
                .setText(campaign.title);
        ((TextView) convertView.findViewById(R.id.shorttext))
                .setText(campaign.shortHeadline);

        Button donate = (Button) convertView.findViewById(R.id.donate);
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ViewCampaignActivity.class);
                intent.putExtra("campaignID", campaign.id);
                intent.putExtra("StripeId", campaign.stripeId);
                view.getContext().startActivity(intent);}
        });
        antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator progressIndicator =
                convertView.findViewById(R.id.circular_progress);
        progressIndicator.setProgress(campaign.raised, campaign.goal);
        return convertView;

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Campaign getItem(int pos) {
        return items.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    Context context;
    List<Campaign> items;
}
