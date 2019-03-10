package com.example.stripe_google_pay;

public class Campaign {
    public Campaign(String body, String title, String shortHeadline, int goal, int raised) {
        this.body = body;
        this.title = title;
        this.shortHeadline = shortHeadline;
        this.goal = goal;
        this.raised = raised;
    }

    public Campaign() {

    }

    public void setId(String id) {
        this.id = id;
    }

    public String body;
    public String title;
    public String shortHeadline;
    public int goal;
    public int raised;
    public String id;
}
