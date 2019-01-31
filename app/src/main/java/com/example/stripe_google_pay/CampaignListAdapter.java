package com.example.stripe_google_pay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CampaignListAdapter extends BaseAdapter {
    //public constructor
    public CampaignListAdapter(Context context, ArrayList<Campaign> items) {
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

        Campaign campaign = getItem(position);
        ((TextView) convertView.findViewById(R.id.title1))
                .setText(campaign.title);
        ((TextView) convertView.findViewById(R.id.shorttext))
                .setText(campaign.shortHeadline);

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
