package com.example.stripe_google_pay;

public class Campaign {
    public Campaign(String title, String shortHeadline, int goal, int raised) {
        this.title = title;
        this.shortHeadline = shortHeadline;
        this.goal = goal;
        this.raised = raised;
    }

    public Campaign() {

    }

    public String title;
    public String shortHeadline;
    public int goal;
    public int raised;
}
