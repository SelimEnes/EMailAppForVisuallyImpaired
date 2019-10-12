package com.selimkilicaslan.e_mailappforvisuallyimpaired;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ContactActivity extends AppCompatActivity {

    EditText contactEditText, emailEditText;
    String label, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        contactEditText = findViewById(R.id.contactEditText);
        emailEditText = findViewById(R.id.emailEditText);
    }

    public void onSaveButtonClick(View view) {
        label = contactEditText.getText().toString();
        email = emailEditText.getText().toString();
        String message;
        if (label == "email") {
            message = "Use another contact name";
        } else {
            if (SharedPreferencesHandler.addContact(getApplicationContext(), label, email)) {
                message = "Contact added";
                finish();
            } else {
                message = "Contact already exist";
            }
        }
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
