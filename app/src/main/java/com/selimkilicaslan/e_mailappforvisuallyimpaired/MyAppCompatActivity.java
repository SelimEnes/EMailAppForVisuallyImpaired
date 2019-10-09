package com.selimkilicaslan.e_mailappforvisuallyimpaired;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;

public class MyAppCompatActivity extends AppCompatActivity {
    AsyncTasks asyncTasks;
    String _Host = "pop.gmail.com";
    Session emailSession;
    public void onAsyncResponse(final int response, Session emailSession){
        this.emailSession = emailSession;
        new Thread(new Runnable() {
            @Override
            public void run() {
                onAsyncReturn(response);
            }
        }).start();
    }
    public void onAsyncReturn(int response){
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        asyncTasks = new AsyncTasks();
    }

    public void logPart(Part part, MailItemParcelable item) throws MessagingException, IOException {
        if (part.isMimeType("text/*")) {
            Log.i("Mail", "Text: " + part.getContent().toString());
            item.setContent(item.getContent() + part.getContent().toString());
        } else if (part.isMimeType("multipart/*")) {
            Multipart content = (Multipart) part.getContent();
            int partCount = content.getCount();
            for (int j = 0; j < partCount; j++) {
                logPart(content.getBodyPart(j), item);
            }
        }
    }
}
