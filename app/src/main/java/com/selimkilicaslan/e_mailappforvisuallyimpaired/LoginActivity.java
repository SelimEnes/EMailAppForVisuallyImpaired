package com.selimkilicaslan.e_mailappforvisuallyimpaired;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

public class LoginActivity extends MyAppCompatActivity {
    String emailAddress, password, _MailStoreType;
    EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        emailAddress = SharedPreferencesHandler.getEmailAddress(getApplicationContext());
        password = SharedPreferencesHandler.getPassword(getApplicationContext());
        if (emailAddress != "" && password != "") {
            //Login(emailAddress, password);
            asyncTasks.Login(this, emailAddress, password, _Host);
        }
    }

    @Override
    public void onAsyncReturn(int functionCode, int response){
        if(functionCode == 1){
            if(response == 1) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }


    public void loginButtonOnClick(View view) {
        emailAddress = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();
        //Login(emailAddress, password);
        asyncTasks.Login(this, emailAddress, password, _Host);
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
                        Toast.makeText(getApplicationContext(), "Successfully logged in", Toast.LENGTH_SHORT).show();
                        SharedPreferencesHandler.setCredentials(getApplicationContext(), email, pass);

                        store.close();
                    }catch (NoSuchProviderException e) {
                        Log.e("Mail",e.getLocalizedMessage());
                    } catch (MessagingException e) {
                        Toast.makeText(getApplicationContext(),"Unauthorized", Toast.LENGTH_SHORT).show();
                        SharedPreferencesHandler.setCredentials(getApplicationContext(), "", "");
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



}
