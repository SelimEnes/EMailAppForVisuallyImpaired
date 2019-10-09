package com.selimkilicaslan.e_mailappforvisuallyimpaired;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

public class LoginActivity extends AppCompatActivity {
    String emailAddress, password, _Host, _MailStoreType;
    EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
    }

    public void loginButtonOnClick(View view) {
        emailAddress = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();
        _Host = "pop.gmail.com";
        Login(emailAddress, password);
    }

    private void Login(final String email, final String pass) {
        try {
            Properties properties = new Properties();

            properties.put("mail.pop3s.host", _Host);
            properties.put("mail.pop3s.port", "995");
            properties.put("mail.pop3s.starttls.enable", "true");


            final Session emailSession = Session.getInstance(properties,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(
                                    email, pass);
                        }
                    });
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        Store store = emailSession.getStore("pop3s");
                        store.connect();

                        // create the folder object and open it
                        Folder emailFolder = store.getFolder("INBOX");
                        emailFolder.open(Folder.READ_ONLY);

                        // retrieve the messages from the folder in an array and print it
                        Message[] messages = emailFolder.getMessages();
                        Log.i("Mail", "messages.length---" + messages.length);
                        Multipart content;
                        for (int i = 0, n = 10; i < n; i++) {
                            Message message = messages[i];
                            //Byte[] contentBuffer = new Byte[message.getContentStream().read()];
                            Log.i("Mail", "---------------------------------");
                            Log.i("Mail", "Email Number " + (i + 1));
                            Log.i("Mail", "Subject: " + message.getSubject());
                            Log.i("Mail", "From: " + message.getFrom()[0]);
//                            if (message.isMimeType("text/plain")) {
//                                Log.i("Mail", "Text: " + message.getContent().toString());
//                            } else if (message.isMimeType("multipart/*")) {
//                                content = (Multipart) message.getContent();
//                                int partCount = content.getCount();
//                                for (int j = 0; j < partCount; j++) {
//                                    Log.i("Mail", "Text: " + j + "-" + content.getBodyPart(j));
//                                }
//                            }
                            logPart(message);
                        }

                        // close the store and folder objects
                        emailFolder.close(false);
                        store.close();
                    }catch (NoSuchProviderException e) {
                        Log.e("Mail",e.getLocalizedMessage());
                    } catch (MessagingException e) {
                        Log.e("Mail",e.getLocalizedMessage());
                    } catch (Exception e) {
                        Log.e("Mail",e.getLocalizedMessage());
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void logPart(Part part) throws MessagingException, IOException {
        if (part.isMimeType("text/plain")) {
            Log.i("Mail", "Text: " + part.getContent().toString());
        } else if (part.isMimeType("multipart/*")) {
            Multipart content = (Multipart) part.getContent();
            int partCount = content.getCount();
            for (int j = 0; j < partCount; j++) {
                logPart(content.getBodyPart(j));
            }
        }
    }

}
