package com.kitanasoftware.interactiveclient.notification;

/**
 * Created by dasha on 11/03/16.
 */
public class MyNotification {

    private String sentTo;
    private String text;

    public MyNotification(String sentTo, String text) {
        this.sentTo = sentTo;
        this.text = text;
    }

    public String getSentTo() {
        return sentTo;
    }

    public void setSentTo(String sentTo) {
        this.sentTo = sentTo;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
