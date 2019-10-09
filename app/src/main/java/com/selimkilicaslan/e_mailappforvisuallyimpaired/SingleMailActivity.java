package com.selimkilicaslan.e_mailappforvisuallyimpaired;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SingleMailActivity extends AppCompatActivity {

    TextView subjectTextView, senderTextView, dateTextView, contentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_mail);
        subjectTextView = findViewById(R.id.subjectTextView);
        senderTextView = findViewById(R.id.senderTextView);
        dateTextView = findViewById(R.id.dateTextView);
        contentTextView = findViewById(R.id.contentTextView);

        MailItemParcelable selectedMail = getIntent().getExtras().getParcelable("Mail");

        subjectTextView.setText(selectedMail.getSubject());
        senderTextView.setText(selectedMail.getSender());
        dateTextView.setText(selectedMail.getDate());
        contentTextView.setText(selectedMail.getContent());
    }
}
