package com.selimkilicaslan.e_mailappforvisuallyimpaired;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SendMailActivity extends MyAppCompatActivity {
    String Subject, Content, Recipient;

    EditText subjectEditText, contentEditText, recipientEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);
        asyncTasks.Login(this, email, password, _Host);
        subjectEditText = findViewById(R.id.subjectEditText);
        contentEditText = findViewById(R.id.contentEditText);
        recipientEditText = findViewById(R.id.recipientEditText);

    }

    @Override
    public void onAsyncReturn(int functionCode, int response) {
        if (functionCode == 2){
            if(response == 1){
                speak("message sent succesfully");
            }
        }
    }

    @Override
    public void onVoiceButtonClick(View view) {
        super.onVoiceButtonClick(view);
    }

    public void sendButtonClick(View view) {
        Subject = subjectEditText.getText().toString();
        Content = contentEditText.getText().toString();
        Recipient = recipientEditText.getText().toString();

        asyncTasks.SendMail(this, emailSession, email, Recipient, Subject, Content);

        //new Thread(new Runnable() {
        //    @Override
        //    public void run() {
        //        try {
        //            runOnUiThread(new Runnable() {
        //                @Override
        //                public void run() {
        //                    Toast.makeText(getApplicationContext(), "Sending E-mail...", Toast.LENGTH_LONG).show();
        //                }
        //            });
        //            MimeMessage mm = new MimeMessage(emailSession);
        //            mm.setFrom(new InternetAddress(email));
        //            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(Recipient));
        //            mm.setSubject(Subject);
        //            mm.setText(Content);
        //            Transport.send(mm);
        //            runOnUiThread(new Runnable() {
        //                @Override
        //                public void run() {
        //                    Toast.makeText(getApplicationContext(), "Successfully Sent E-mail", Toast.LENGTH_SHORT).show();
        //                }
        //            });
        //        }catch (Exception ex){
        //            runOnUiThread(new Runnable() {
        //                @Override
        //                public void run() {
        //                    Toast.makeText(getApplicationContext(), "Couldn't send e-mail", Toast.LENGTH_SHORT).show();
        //                }
        //            });
        //        }
        //    }
        //}).start();
    }
}
